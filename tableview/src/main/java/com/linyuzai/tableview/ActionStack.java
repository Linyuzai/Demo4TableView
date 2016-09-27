package com.linyuzai.tableview;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/27 0027.
 */

class ActionStack {

    private List<Action> actions = new ArrayList<>();

    public void push(int fromPosition, int toPosition) {
        actions.add(0, new Action(fromPosition, toPosition));
    }

    public Action pop() {
        Action action = actions.get(0);
        actions.remove(0);
        return action;
    }

    public boolean isEmpty() {
        return actions.isEmpty();
    }

    public void clear() {
        actions.clear();
    }

    class Action {
        int fromPosition;
        int toPosition;

        public Action(int fromPosition, int toPosition) {
            this.fromPosition = fromPosition;
            this.toPosition = toPosition;
        }
    }
}
