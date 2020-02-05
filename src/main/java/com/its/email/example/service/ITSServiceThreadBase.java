package com.its.email.example.service;

public abstract class ITSServiceThreadBase {
	protected int miThreadState = 0;
	private long threadId;

	public long getThreadId() {
		return threadId;
	}

	public void setThreadId(long threadId) {
		this.threadId = threadId;
	}

	public void start() {
		this.miThreadState = 1;
		Runnable r = new Runnable() {
			public void run() {
				runThread();
			}
		};
		Thread t = new Thread(r);
		t.start();
		this.threadId = t.getId();
	}

	public void stop() {
		this.miThreadState = 0;
	}

	public void runThread() {
		while (this.miThreadState != 0) {
			try {
				init();
				process();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					end();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	// protected
	protected abstract void init() throws Exception;

	protected abstract void process() throws Exception;

	protected abstract void end() throws Exception;
}
