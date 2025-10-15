package scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

// Lê os arquivos 01..10 e monta a lista de instruções. Também lê o quantum.
public class CarregadorProgramas {
    private static final Pattern PADRAO_NUM = Pattern.compile("\\d{2}\\.txt");

    public List<BCP> carregarProcessos(File dir) throws IOException {
        File[] arquivos = dir.listFiles(f -> PADRAO_NUM.matcher(f.getName()).matches());
        if (arquivos == null) return List.of();
        List<File> ordenados = new ArrayList<>(List.of(arquivos));
        ordenados.sort(Comparator.comparing(File::getName));
        List<BCP> lista = new ArrayList<>();
        for (File f: ordenados) lista.add(parse(f));
        return lista;
    }

    private BCP parse(File f) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String nome = br.readLine();
            if (nome == null) nome = f.getName();
            List<Instrucao> codigo = new ArrayList<>();
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.equals("COM")) codigo.add(Instrucao.com());
                else if (line.equals("E/S")) codigo.add(Instrucao.es());
                else if (line.equals("SAIDA")) { codigo.add(Instrucao.saida()); break; }
                else if (line.startsWith("X=")) {
                    int v = Integer.parseInt(line.substring(2)); codigo.add(Instrucao.atribX(v));
                } else if (line.startsWith("Y=")) {
                    int v = Integer.parseInt(line.substring(2)); codigo.add(Instrucao.atribY(v));
                }
            }
            return new BCP(nome, codigo);
        }
    }

    public int carregarQuantum(File dir) throws IOException {
        File q = new File(dir, "quantum.txt");
        try (BufferedReader br = new BufferedReader(new FileReader(q))) {
            return Integer.parseInt(br.readLine().trim());
        }
    }
}
