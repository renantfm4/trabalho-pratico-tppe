package br.unb.tppe.curation;

public class VerificadorIniciaisOrdenadas {

    private final ComparadorNomes comparador;
    private final String completo;
    private final String abreviado;
    private String[] partesComp;
    private String[] partesAbrev;
    private int idxComp;

    public VerificadorIniciaisOrdenadas(ComparadorNomes comparador, String completo, String abreviado) {
        this.comparador = comparador;
        this.completo = completo;
        this.abreviado = abreviado;
    }

    public boolean compute() {
        partesComp = completo.split(" ");
        partesAbrev = abreviado.split(" ");

        if (partesAbrev.length < 2 || partesComp.length < partesAbrev.length) {
            return false;
        }

        if (!partesAbrev[partesAbrev.length - 1].equals(
                partesComp[partesComp.length - 1]
        )) {
            return false;
        }

        idxComp = 0;
        for (String tokenAbrev : partesAbrev) {
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
}
