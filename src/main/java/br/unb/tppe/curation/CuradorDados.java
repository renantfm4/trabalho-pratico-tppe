package br.unb.tppe.curation;

import java.util.ArrayList;
import java.util.List;

public class CuradorDados {

    private final ComparadorNomes comparador = new ComparadorNomes();

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

                if (comparador.saoMesmoAutor(r1.getNome(), r2.getNome())) {
                    String nomeCompleto = comparador.escolherFormaCompleta(
                            r1.getNome(),
                            r2.getNome()
                    );
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
        if (nome == null) {
            return "";
        }
        return nome.replace("’", "'")
                .replace("`", "'")
                .replaceAll("\\s+", " ")
                .trim();
    }
}