package msrcpsp.Tabu;

import msrcpsp.scheduling.BaseIndividual;

import java.util.LinkedList;
import java.util.List;

public class SinkingList {
    final int size;
    LinkedList<BaseIndividual> list;

    SinkingList(int size) {
        this.size = size;
        list = new LinkedList<>();
    }

    public void add(BaseIndividual baseIndividual) {
        if (list.size() == size) {
            list.removeFirst();
        }
        list.add(baseIndividual);
    }

    public List<BaseIndividual> getList() {
        return list;
    }
}
