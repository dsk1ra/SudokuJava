class Timer {
    private long startTime;
    private boolean running;

    public Timer() {
        this.running = false;
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        this.running = true;
    }

    public void stop() {
        this.running = false;
    }

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