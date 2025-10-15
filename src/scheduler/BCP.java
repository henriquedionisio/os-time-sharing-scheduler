package scheduler;

import java.util.List;

// BCP: onde eu guardo tudo que preciso para retomar o processo depois.
public class BCP {
    private final String nome;
    private final List<Instrucao> codigo; // "memória" do programa (só texto)
    private int pc = 0;
    private int regX = 0;
    private int regY = 0;
    private EstadoProcesso estado = EstadoProcesso.READY;
    private int esperaIO = 0; // contador de espera de E/S (2 -> 1 -> 0)

    public BCP(String nome, List<Instrucao> codigo) { this.nome = nome; this.codigo = codigo; }

    public String getNome() { return nome; }
    public int getPc() { return pc; }
    public int getRegX() { return regX; }
    public int getRegY() { return regY; }
    public EstadoProcesso getEstado() { return estado; }
    public boolean terminou() { return estado == EstadoProcesso.FINISHED; }

    public void tickEsperaIO() {
        if (esperaIO > 0) {
            esperaIO--;
            if (esperaIO == 0 && estado == EstadoProcesso.BLOCKED) estado = EstadoProcesso.READY;
        }
    }

    public ExecucaoResultado executaUma() {
        if (terminou()) return ExecucaoResultado.FINISHED;
        if (pc >= codigo.size()) { estado = EstadoProcesso.FINISHED; return ExecucaoResultado.FINISHED; }
        Instrucao inst = codigo.get(pc);
        estado = EstadoProcesso.RUNNING;
        switch (inst.getTipo()) {
            case COM -> { pc++; return ExecucaoResultado.NORMAL; }
            case ATRIB_X -> { regX = inst.getValor(); pc++; return ExecucaoResultado.NORMAL; }
            case ATRIB_Y -> { regY = inst.getValor(); pc++; return ExecucaoResultado.NORMAL; }
            case ES -> { pc++; estado = EstadoProcesso.BLOCKED; esperaIO = 2; return ExecucaoResultado.IO; }
            case SAIDA -> { pc++; estado = EstadoProcesso.FINISHED; return ExecucaoResultado.FINISHED; }
        }
    return ExecucaoResultado.NORMAL; // só para o compilador não reclamar
    }

    public enum ExecucaoResultado { NORMAL, IO, FINISHED }
}
