/**
 * Represents a timer that can measure elapsed time.
 */
class Timer {
    private long startTime;
    private boolean running;

    /**
     * Constructs a new Timer object.
     */
    public Timer() {
        this.running = false;
    }

    /**
     * Starts the timer.
     */
    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    /**
     * Stops the timer.
     */
    public void stop() {
        this.running = false;
    }

    /**
     * Returns the elapsed time since the timer was started, in the format "X minutes and Y seconds".
     *
     * @return the elapsed time
     */
    public String getElapsedTime() {
        if (this.running) {
            long currentTime = System.currentTimeMillis();
            long elapsedTime = currentTime - this.startTime;
            int seconds = (int) (elapsedTime / 1000) % 60;
            int minutes = (int) (elapsedTime / (1000 * 60)) % 60;
            return String.format("%d minutes and %d seconds", minutes, seconds);
        } else {
            return "0 minutes and 0 seconds";
        }
    }
}