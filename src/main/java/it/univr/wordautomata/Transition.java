package it.univr.wordautomata;

/**
 *
 * @author Lorenzo
 */
public class Transition {
    private String label;
    
    public Transition(String label){
        this.label = label;
    }
    
    public String getLabel(){
        return label;
    }
    
    public void setLabel(String label){
        this.label = label;
    }
    
    @Override
    public String toString(){
        return label;
    }
}
