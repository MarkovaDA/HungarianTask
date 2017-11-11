
package myhungarianalgorithm;

/**
 *
 * @author markova
 */
public class Edge {
    private int start;
    private int end;
    private boolean direct;//прямое направление или обратное
    
    public Edge(int start, int end, boolean isDirect) {
        this.start = start;
        this.end = end;
        this.direct = isDirect;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int nodeStart) {
        this.start = nodeStart;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int nodeEnd) {
        this.end = nodeEnd;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setIsDirect(boolean isDirect) {
        this.direct = isDirect;
    }

    @Override
    public String toString() {
        return "Edge{ " + "(" + start + "," + end + "), direct=" + direct + '}';
    }
    
    
    
}
