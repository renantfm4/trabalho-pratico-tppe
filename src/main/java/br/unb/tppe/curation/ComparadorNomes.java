package br.unb.tppe.curation;

import java.text.Normalizer;

public class ComparadorNomes {

    public boolean saoMesmoAutor(String n1, String n2) {
        String s1 = simplificar(n1);
        String s2 = simplificar(n2);
        if (s1.equals(s2)) {
            return true;
        }

        return verificarIniciais(s1, s2)
                || verificarIniciais(s2, s1)
                || verificarIniciaisOrdenadas(s1, s2)
                || verificarIniciaisOrdenadas(s2, s1)
                || verificarIniciaisAgrupadas(s1, s2)
                || verificarIniciaisAgrupadas(s2, s1);
    }

    private String simplificar(String nome) {
        String s = Normalizer.normalize(
                nome.toLowerCase(),
                Normalizer.Form.NFD
        ).replaceAll("[^\\p{ASCII}]", "");
        s = s.replace(".", " ").replaceAll("\\s+", " ").trim();
        s = s.replaceAll("\\b(de|da|do|dos|das)\\b", "")
                .replaceAll("\\s+", " ")
                .trim();
        return s;
    }

    private boolean verificarIniciais(String completo, String abreviado) {
        String[] partesComp = completo.split(" ");
        String[] partesAbrev = abreviado.split(" ");
        if (partesAbrev.length < 2 || partesComp.length < partesAbrev.length) {
            return false;
        }

        if (partesAbrev[0].equals(partesComp[partesComp.length - 1])) {
            int idxComp = 0;
            for (int i = 1; i < partesAbrev.length; i++) {
                if (partesAbrev[i].length() == 1
                        && idxComp < partesComp.length - 1) {
                    if (partesComp[idxComp].startsWith(partesAbrev[i])) {
                        idxComp++;
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private boolean verificarIniciaisOrdenadas(
            String completo,
            String abreviado
    ) {
        String[] partesComp = completo.split(" ");
        String[] partesAbrev = abreviado.split(" ");

        if (partesAbrev.length < 2 || partesComp.length < partesAbrev.length) {
            return false;
        }

        if (!partesAbrev[partesAbrev.length - 1].equals(
                partesComp[partesComp.length - 1]
        )) {
            return false;
        }

        int idxComp = 0;
        for (int i = 0; i < partesAbrev.length; i++) {
            String tokenAbrev = partesAbrev[i];
            boolean matched = false;

            while (idxComp < partesComp.length) {
                String tokenComp = partesComp[idxComp];
                if (tokenAbrev.equals(tokenComp)
                        || (tokenAbrev.length() == 1
                        && tokenComp.startsWith(tokenAbrev))) {
                    matched = true;
                    idxComp++;
                    break;
                }
                idxComp++;
            }
            if (!matched) {
                return false;
            }
        }
        return true;
    }

    private boolean verificarIniciaisAgrupadas(
            String completo,
            String abreviado
    ) {
        String[] partesComp = completo.split(" ");
        String[] partesAbrev = abreviado.split(" ");
        if (partesAbrev.length != 2) {
            return false;
        }

        String iniciais = partesAbrev[0];
        String ultimoSobrenome = partesAbrev[1];

        if (!partesComp[partesComp.length - 1].equals(ultimoSobrenome)) {
            return false;
        }
        if (iniciais.length() != partesComp.length - 1) {
            return false;
        }

        for (int i = 0; i < iniciais.length(); i++) {
            if (partesComp[i].charAt(0) != iniciais.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public String escolherFormaCompleta(String n1, String n2) {
        int acentos1 = contarAcentos(n1);
        int acentos2 = contarAcentos(n2);
        if (acentos1 != acentos2) {
            return acentos1 > acentos2 ? n1 : n2;
        }

        int pontos1 = n1.length() - n1.replace(".", "").length();
        int pontos2 = n2.length() - n2.replace(".", "").length();
        if (pontos1 != pontos2) {
            return pontos1 < pontos2 ? n1 : n2;
        }

        boolean temParticula1 = n1.toLowerCase()
                .matches(".*\\b(de|da|do)\\b.*");
        boolean temParticula2 = n2.toLowerCase()
                .matches(".*\\b(de|da|do)\\b.*");
        if (temParticula1 != temParticula2) {
            return temParticula1 ? n1 : n2;
        }

        return n1.length() >= n2.length() ? n1 : n2;
    }

    private int contarAcentos(String s) {
        if (s == null) {
            return 0;
        }
        return s.length() - s.replaceAll("[^\u0000-\u007F]", "").length();
    }
}