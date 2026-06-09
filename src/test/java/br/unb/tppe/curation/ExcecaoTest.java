package br.unb.tppe.curation;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Excecoes")
public class ExcecaoTest {

    @Test
    public void testExcecoesDeInstanciacao() {
        assertThrows(IllegalArgumentException.class, () -> {
            new RegistroAutor(-5, "Luiz");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new RegistroAutor(10, "");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new CuradorDados().processar(null);
        });
    }
}