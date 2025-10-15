package scheduler;

// Uma instrução do "mini processador". Só guarda tipo e, quando for atribuição, o valor.
public class Instrucao {
    private final TipoInstrucao tipo;
    private final int valor; // usado apenas em atribuições

    private Instrucao(TipoInstrucao tipo, int valor) { this.tipo = tipo; this.valor = valor; }

    public static Instrucao com() { return new Instrucao(TipoInstrucao.COM, 0); }
    public static Instrucao es() { return new Instrucao(TipoInstrucao.ES, 0); }
    public static Instrucao saida() { return new Instrucao(TipoInstrucao.SAIDA, 0); }
    public static Instrucao atribX(int v) { return new Instrucao(TipoInstrucao.ATRIB_X, v); }
    public static Instrucao atribY(int v) { return new Instrucao(TipoInstrucao.ATRIB_Y, v); }

    public TipoInstrucao getTipo() { return tipo; }
    public int getValor() { return valor; }
}
