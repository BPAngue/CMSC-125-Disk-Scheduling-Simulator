package blockify;

public interface SimulationContext {
    void updateSeekTimeLabel(int totalSeekTime);
    void updateSeekTimeLabelFcfs(int totalSeekTime);
    void updateSeekTimeLabelSstf(int totalSeekTime);
    void updateSeekTimeLabelScan(int totalSeekTime);
    void updateSeekTimeLabelCscan(int totalSeekTime);
    void updateSeekTimeLabelLook(int totalSeekTime);
    void updateSeekTimeLabelClook(int totalSeekTime);
    void updateTimerLabel(int minutes, int seconds);
    void updateTimerLabelFcfs(int minutes, int seconds);
    void updateTimerLabelSstf(int minutes, int seconds);
    void updateTimerLabelScan(int minutes, int seconds);
    void updateTimerLabelCscan(int minutes, int seconds);
    void updateTimerLabelLook(int minutes, int seconds);
    void updateTimerLabelClook(int minutes, int seconds);
    void markSimulationFinished();
}
