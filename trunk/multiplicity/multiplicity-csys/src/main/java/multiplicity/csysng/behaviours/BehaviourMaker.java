package multiplicity.csysng.behaviours;

import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import multiplicity.csysng.items.IItem;

public class BehaviourMaker {

    private static final Logger log = Logger.getLogger(BehaviourMaker.class
            .getName());

    public static IBehaviour addBehaviour(IItem item,
            Class<? extends IBehaviour> behaviourClass) {
        try {
            IBehaviour behaviour = behaviourClass.newInstance();
            behaviour.setItemActingOn(item);
            item.behaviourAdded(behaviour);
            return behaviour;
        } catch (InstantiationException e) {
            log.warning(e.toString());
        } catch (IllegalAccessException e) {
            log.warning(e.toString());
        }
        return null;
    }

    public static void removeBehavior(IItem item,
            Class<? extends IBehaviour> behaviourClass) {
        if (!item.getBehaviours().isEmpty()) {
            List<IBehaviour> behaviours = item.getBehaviours();
            for (IBehaviour be : behaviours) {
                if (be.getClass().equals(behaviourClass)) {
                    item.getBehaviours().remove(be);
                }
            }

        }
    }
}
