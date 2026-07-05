package br.unb.tppe.curation;

import java.util.ArrayList;
import java.util.List;

public class CuradorDados {

    private final ComparadorNomes comparador = new ComparadorNomes();

    public List<RegistroAutor> processar(List<RegistroAutor> registros) {
        validarRegistros(registros);

        List<RegistroAutor> resultado = clonarRegistros(registros);

        // Caso 1: Tipografia (Adequação de caracteres especiais)
        corrigirTipografiaDosRegistros(resultado);

        // Casos 2, 3 e 4: Unificar nomes equivalentes
        unificarNomesEquivalentes(resultado);

        // Caso 5: IDs pelo menor valor
        ajustarIdsPeloMenorValor(resultado);

        return resultado;
    }

    // --- Métodos Extraídos ---

    private void validarRegistros(List<RegistroAutor> registros) {
        if (registros == null) {
            throw new IllegalArgumentException("A lista nao pode ser nula.");
        }
    }

    private List<RegistroAutor> clonarRegistros(List<RegistroAutor> registros) {
        List<RegistroAutor> resultado = new ArrayList<>();
        for (RegistroAutor r : registros) {
            resultado.add(new RegistroAutor(r.getId(), r.getNome()));
        }
        return resultado;
    }

    private void corrigirTipografiaDosRegistros(List<RegistroAutor> registros) {
        for (RegistroAutor r : registros) {
            r.setNome(corrigirTipografia(r.getNome()));
        }
    }

    private void unificarNomesEquivalentes(List<RegistroAutor> registros) {
        for (int i = 0; i < registros.size(); i++) {
            for (int j = i + 1; j < registros.size(); j++) {
                RegistroAutor r1 = registros.get(i);
                RegistroAutor r2 = registros.get(j);

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
    }

    private void ajustarIdsPeloMenorValor(List<RegistroAutor> registros) {
        for (int i = 0; i < registros.size(); i++) {
            for (int j = i + 1; j < registros.size(); j++) {
                RegistroAutor r1 = registros.get(i);
                RegistroAutor r2 = registros.get(j);

                if (r1.getNome().equalsIgnoreCase(r2.getNome())) {
                    int menorId = Math.min(r1.getId(), r2.getId());
                    r1.setId(menorId);
                    r2.setId(menorId);
                }
            }
        }
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