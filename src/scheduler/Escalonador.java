package scheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

// Arquivo principal exigido: lê programas e roda o Round Robin para um quantum.
public class Escalonador {
    public static void main(String[] args) throws Exception {
        File base = new File("programas");
        CarregadorProgramas loader = new CarregadorProgramas();
        if (!base.exists()) throw new IllegalStateException("Diretorio 'programas' inexistente");
        int q = loader.carregarQuantum(base);
            var procs = loader.carregarProcessos(base);
            if (procs.isEmpty()) throw new IllegalStateException("Nenhum processo carregado");
            String logName = q < 10 ? String.format("log0%d.txt", q) : String.format("log%d.txt", q);
            try (PrintStream ps = new PrintStream(logName)) {
                new EscalonadorRoundRobin(procs, q).executar(ps);
            } catch (FileNotFoundException e) {
                System.err.println("Não foi possível criar logfile: " + e.getMessage());
            }
    }
}
