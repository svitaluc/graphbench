package eu.profinit.manta.graphbench.data.generator.model;

public class Node extends Entity {
    public static final String FILE_NAME = "node.csv";

    private Long parent;
    private String name;

    public void setParent(Long id) {
        this.parent = id;
    }

    public void setName(String name) {
        this.name = name;
    };

    @Override
    public String[] getStringSet() {
        if (parent != null) {
            return new String[]{id.toString(), parent.toString(), name};
        }
        return new String[]{id.toString(), "", name};
    }
}
