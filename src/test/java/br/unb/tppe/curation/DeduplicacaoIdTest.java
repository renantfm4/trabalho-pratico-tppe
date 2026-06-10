package br.unb.tppe.curation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Caso5Id")
public class DeduplicacaoIdTest {

    @Test
    public void testUnificacaoIdsPeloMenorValor() {
        CuradorDados curador = new CuradorDados();
        List<RegistroAutor> autores = Arrays.asList(
                new RegistroAutor(433094, "Raphael Gonçalves Viana"),
                new RegistroAutor(31298, "Raphael Goncalves Viana"),
                new RegistroAutor(549243, "Raphael Gonçalves Viana"),
                new RegistroAutor(608297, "Raphael Gonçalves Viana")
        );

        List<RegistroAutor> resultado = curador.processar(autores);

        assertEquals(4, resultado.size());
        for (RegistroAutor r : resultado) {
            assertEquals(31298, r.getId());
            assertEquals("Raphael Gonçalves Viana", r.getNome());
        }
    }

    @Test
    public void testUnificacaoIdsPeloMenorValorLilian() {
        CuradorDados curador = new CuradorDados();
        List<RegistroAutor> autores = Arrays.asList(
                new RegistroAutor(899639, "Lilian Luíza Viana Vieira"),
                new RegistroAutor(243351, "Lílian Luíza Viana Vieira"),
                new RegistroAutor(663795, "Lílian Luíza Viana Vieira")
        );

        List<RegistroAutor> resultado = curador.processar(autores);

        assertEquals(3, resultado.size());
        for (RegistroAutor r : resultado) {
            assertEquals(243351, r.getId());
            assertEquals("Lílian Luíza Viana Vieira", r.getNome());
        }
    }
}