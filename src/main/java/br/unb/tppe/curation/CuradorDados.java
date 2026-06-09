package br.unb.tppe.curation;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class CuradorDados {

    public List<RegistroAutor> processar(List<RegistroAutor> registros) {
        if (registros == null) {
            throw new IllegalArgumentException("A lista nao pode ser nula.");
        }

        List<RegistroAutor> resultado = new ArrayList<>();
        for (RegistroAutor r : registros) {
            resultado.add(new RegistroAutor(r.getId(), r.getNome()));
        }

        // Caso 1: Tipografia (Adequação de caracteres especiais)
        for (RegistroAutor r : resultado) {
            r.setNome(corrigirTipografia(r.getNome()));
        }

        // Casos 2, 3 e 4: Unificar nomes equivalentes
        for (int i = 0; i < resultado.size(); i++) {
            for (int j = i + 1; j < resultado.size(); j++) {
                RegistroAutor r1 = resultado.get(i);
                RegistroAutor r2 = resultado.get(j);

                if (saoMesmoAutor(r1.getNome(), r2.getNome())) {
                    String nomeCompleto = escolherFormaCompleta(r1.getNome(), r2.getNome());
                    r1.setNome(nomeCompleto);
                    r2.setNome(nomeCompleto);
                }
            }
        }

        // Caso 5: IDs pelo menor valor
        for (int i = 0; i < resultado.size(); i++) {
            for (int j = i + 1; j < resultado.size(); j++) {
                RegistroAutor r1 = resultado.get(i);
                RegistroAutor r2 = resultado.get(j);

                if (r1.getNome().equalsIgnoreCase(r2.getNome())) {
                    int menorId = Math.min(r1.getId(), r2.getId());
                    r1.setId(menorId);
                    r2.setId(menorId);
                }
            }
        }

        return resultado;
    }

    private String corrigirTipografia(String nome) {
        if (nome == null) return "";
        // Corrige de forma dinâmica os apóstrofos e espaços
        return nome.replace("’", "'").replace("`", "'").replaceAll("\\s+", " ").trim();
    }

    private boolean saoMesmoAutor(String n1, String n2) {
        String s1 = simplificar(n1);
        String s2 = simplificar(n2);
        if (s1.equals(s2)) return true;

        return verificarIniciais(s1, s2) ||
                verificarIniciais(s2, s1) ||
                verificarIniciaisOrdenadas(s1, s2) ||
                verificarIniciaisOrdenadas(s2, s1) ||
                verificarIniciaisAgrupadas(s1, s2) ||
                verificarIniciaisAgrupadas(s2, s1);
    }

    private String simplificar(String nome) {
        String s = Normalizer.normalize(nome.toLowerCase(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        s = s.replace(".", " ").replaceAll("\\s+", " ").trim();
        s = s.replaceAll("\\b(de|da|do|dos|das)\\b", "").replaceAll("\\s+", " ").trim();
        return s;
    }

    // Valida abreviações invertidas (ex: "Seabra A. M.")
    private boolean verificarIniciais(String completo, String abreviado) {
        String[] partesComp = completo.split(" ");
        String[] partesAbrev = abreviado.split(" ");
        if (partesAbrev.length < 2 || partesComp.length < partesAbrev.length) return false;

        if (partesAbrev[0].equals(partesComp[partesComp.length - 1])) {
            int idxComp = 0;
            for (int i = 1; i < partesAbrev.length; i++) {
                if (partesAbrev[i].length() == 1 && idxComp < partesComp.length - 1) {
                    if (partesComp[idxComp].startsWith(partesAbrev[i])) {
                        idxComp++;
                    } else { return false; }
                }
            }
            return true;
        }
        return false;
    }

    // Valida iniciais ordenadas no fluxo do texto (ex: "Luiz de O. de Souza")
    private boolean verificarIniciaisOrdenadas(String completo, String abreviado) {
        String[] partesComp = completo.split(" ");
        String[] partesAbrev = abreviado.split(" ");

        if (partesAbrev.length < 2 || partesComp.length < partesAbrev.length) return false;

        // Garante que o último sobrenome bate
        if (!partesAbrev[partesAbrev.length - 1].equals(partesComp[partesComp.length - 1])) {
            return false;
        }

        int idxComp = 0;
        for (int i = 0; i < partesAbrev.length; i++) {
            String tokenAbrev = partesAbrev[i];
            boolean matched = false;

            while (idxComp < partesComp.length) {
                String tokenComp = partesComp[idxComp];
                if (tokenAbrev.equals(tokenComp) || (tokenAbrev.length() == 1 && tokenComp.startsWith(tokenAbrev))) {
                    matched = true;
                    idxComp++;
                    break;
                }
                idxComp++;
            }
            if (!matched) return false;
        }
        return true;
    }

    // Valida iniciais agrupadas (ex: "VC Junior")
    private boolean verificarIniciaisAgrupadas(String completo, String abreviado) {
        String[] partesComp = completo.split(" ");
        String[] partesAbrev = abreviado.split(" ");
        if (partesAbrev.length != 2) return false;

        String iniciais = partesAbrev[0];
        String ultimoSobrenome = partesAbrev[1];

        if (!partesComp[partesComp.length - 1].equals(ultimoSobrenome)) return false;
        if (iniciais.length() != partesComp.length - 1) return false;

        for (int i = 0; i < iniciais.length(); i++) {
            if (partesComp[i].charAt(0) != iniciais.charAt(i)) return false;
        }
        return true;
    }

    private String escolherFormaCompleta(String n1, String n2) {
        // Dinamicamente escolhe a versão que de fato mantém a acentuação original brasileira
        int acentos1 = contarAcentos(n1);
        int acentos2 = contarAcentos(n2);
        if (acentos1 != acentos2) {
            return acentos1 > acentos2 ? n1 : n2;
        }

        // Elimina pontos de abreviação
        int pontos1 = n1.length() - n1.replace(".", "").length();
        int pontos2 = n2.length() - n2.replace(".", "").length();
        if (pontos1 != pontos2) return pontos1 < pontos2 ? n1 : n2;

        // Escolhe a que possui partículas de ligação ("de", "da", "do")
        boolean temParticula1 = n1.toLowerCase().matches(".*\\b(de|da|do)\\b.*");
        boolean temParticula2 = n2.toLowerCase().matches(".*\\b(de|da|do)\\b.*");
        if (temParticula1 != temParticula2) return temParticula1 ? n1 : n2;

        return n1.length() >= n2.length() ? n1 : n2;
    }

    private int contarAcentos(String s) {
        if (s == null) return 0;
        // Subtrai do comprimento total os caracteres estritamente ASCII
        return s.length() - s.replaceAll("[^\u0000-\u007F]", "").length();
    }
}