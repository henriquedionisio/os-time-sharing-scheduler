package scheduler;

import java.io.PrintStream;
import java.util.List;

// Loop principal do Round Robin.
public class EscalonadorRoundRobin {
    private final TabelaProcessos tabela;
    private final int quantum;
    private int totalQuanta = 0;
    private int instrucoesExecutadas = 0; // conto tudo: COM, atrib, E/S, SAIDA

    public EscalonadorRoundRobin(List<BCP> processos, int quantum) {
        this.tabela = new TabelaProcessos(processos);
        this.quantum = quantum;
    }

    public void executar(PrintStream out) {
        for (BCP p: tabela.getTodos()) out.println("Carregando " + p.getNome());
        while (!tabela.terminouTudo()) {
            tabela.tickBloqueados();
            if (tabela.semProntos()) { // se só tenho bloqueados, só avanço o "tempo" de E/S
                tabela.tickBloqueados();
                continue;
            }
            BCP atual = tabela.proximoPronto();
            if (atual == null) continue;
            out.println("Executando " + atual.getNome());
            int executadasNesteQuantum = 0;
            boolean terminou = false;
            boolean bloqueou = false;
            while (executadasNesteQuantum < quantum) {
                BCP.ExecucaoResultado r = atual.executaUma();
                if (r == BCP.ExecucaoResultado.NORMAL) {
                    executadasNesteQuantum++; instrucoesExecutadas++;
                } else if (r == BCP.ExecucaoResultado.IO) {
                    executadasNesteQuantum++; instrucoesExecutadas++; bloqueou = true; out.println("E/S iniciada em " + atual.getNome());
                    break;
                } else if (r == BCP.ExecucaoResultado.FINISHED) {
                    executadasNesteQuantum++; instrucoesExecutadas++; terminou = true; out.println(atual.getNome() + " terminado. X=" + atual.getRegX() + ". Y=" + atual.getRegY());
                    break;
                }
            }
            totalQuanta++;
            if (!terminou) out.println(interrompendoLinha(atual.getNome(), executadasNesteQuantum));
            if (bloqueou) tabela.adicionarBloqueado(atual); else if (!terminou) tabela.adicionarPronto(atual);
            tabela.tickBloqueados();
        }
        out.println("MEDIA DE TROCAS: " + mediaTrocas());
        out.println("MEDIA DE INSTRUCOES: " + mediaInstrucoes());
        out.println("QUANTUM: " + quantum);
    }

    private String interrompendoLinha(String nome, int instr) {
        if (instr == 1) return "Interrompendo " + nome + " após 1 instrução";
        return "Interrompendo " + nome + " após " + instr + " instruções";
    }

    private double mediaTrocas() { // total de "fatias" dividido pelo nº de processos
        int n = tabela.getTodos().size();
        if (n == 0) return 0.0;
        return (double) totalQuanta / (double) n;
    }
    private double mediaInstrucoes() {
        if (totalQuanta == 0) return 0.0;
        return (double) instrucoesExecutadas / (double) totalQuanta;
    }
}
