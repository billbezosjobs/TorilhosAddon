package mdsol.torilhosaddon.feature.model;

import io.wispforest.owo.util.Observable;

import java.util.Objects;

public class TrackedBoss extends Observable<TrackedBoss> {

    private final BossData data;
    private String calledPlayerName;
    private State state;
    private int portalTimer = 0;
    private double distanceMarkerValue = 0;

    public TrackedBoss(BossData data, State state) {
        super(null);
        this.data = data;
        this.state = state;
    }

    public BossData getData() {
        return data;
    }

    public String getCalledPlayerName() {
        return calledPlayerName;
    }

    public void setCalledPlayerName(String calledPlayerName) {
        if (!Objects.equals(this.calledPlayerName, calledPlayerName)) {
            this.calledPlayerName = calledPlayerName;
            notifyObservers(this);
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        if (!Objects.equals(this.state, state)) {
            this.state = state;
            notifyObservers(this);
        }
    }

    public int getPortalTimer() {
        return portalTimer;
    }

    public void resetPortalTimer() {
        portalTimer = 30 * 20;
        notifyObservers(this);
    }

    public int decrementPortalTimer() {
        portalTimer -= 1;
        notifyObservers(this);
        return portalTimer;
    }

    public double getDistanceMarkerValue() {
        return distanceMarkerValue;
    }

    public void setDistanceMarkerValue(double distanceMarkerValue) {
        if (!Objects.equals(this.distanceMarkerValue, distanceMarkerValue)) {
            this.distanceMarkerValue = distanceMarkerValue;
            notifyObservers(this);
        }
    }

    public enum State {
        ALIVE,
        JUST_DEFEATED,
        DEFEATED
    }
}
