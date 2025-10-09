package chessopeningtrainer.service;

import chessopeningtrainer.view.Refreshable;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used as an Observer, so you can refresh the GUI from every Part of the Service Layer
 */
public abstract class AbstractRefreshingService {
    public List<Refreshable> refreshables = new ArrayList<>();

    public void addRefreshable(Refreshable r) {
        refreshables.add(r);
    }
}
