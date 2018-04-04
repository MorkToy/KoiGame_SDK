package koigame.sdk.api;

public class KThread extends Thread {

	private boolean isDestory = false;

	private Runnable runnable;

	public KThread() {
		super();
	}

	public KThread(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public void run() {
		runnable.run();
	}

	public void safeDestory() {
		isDestory = true;
	}

	public boolean isDestory() {
		return isDestory;
	}

}
