package scheduler;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

// Guardo todos os processos e as filas (prontos / bloqueados).
public class TabelaProcessos {
    private final List<BCP> todos;
    private final Deque<BCP> prontos = new ArrayDeque<>();
    private final List<BCP> bloqueados = new ArrayList<>();

    public TabelaProcessos(List<BCP> todos) {
        this.todos = todos;
        prontos.addAll(todos);
    }
    public boolean terminouTudo() { return todos.stream().allMatch(BCP::terminou); }
    public BCP proximoPronto() { return prontos.pollFirst(); }
    public void adicionarPronto(BCP p) { prontos.addLast(p); }
    public void adicionarBloqueado(BCP p) { bloqueados.add(p); }
    public boolean semProntos() { return prontos.isEmpty(); }
    public List<BCP> getTodos() { return todos; }

    public void tickBloqueados() {
        if (bloqueados.isEmpty()) return;
        for (BCP p: bloqueados) p.tickEsperaIO();
        var liberados = bloqueados.stream().filter(p -> p.getEstado() == EstadoProcesso.READY).toList();
        if (!liberados.isEmpty()) {
            bloqueados.removeAll(liberados);
            for (var p: liberados) prontos.addLast(p);
        }
    }
}
