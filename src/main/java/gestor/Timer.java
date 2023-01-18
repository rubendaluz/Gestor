/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestor;

/**
 *
 * @author vanildo9
 */
public class Timer {
    private boolean flag = false;
    public void countDown(int time) {
        new Thread(() -> {
            try {
                Thread.sleep(time);
                flag = true;
            } catch (InterruptedException e) {
            }
        }).start();
    }
    public boolean getFlag() {
        return flag;
    }
}
