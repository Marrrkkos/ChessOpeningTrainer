package chessopeningtrainer.service;

import chessopeningtrainer.view.Refreshable;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRefreshingService {
    public List<Refreshable> refreshables = new ArrayList<>();

    public void addRefreshable(Refreshable r) {
        refreshables.add(r);
    }
}
