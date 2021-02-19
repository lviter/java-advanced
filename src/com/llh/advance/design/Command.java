package com.llh.advance.design;

public class Command {
    public static void main(String[] args) {
        AbstractCommand cmd = new ConcreteCommand();
        Invoker ir = new Invoker(cmd);
        System.out.println("客户访问调用者的call()方法...");
        ir.call();
        ir.add();
    }


}

/**
 * 调用者
 */
class Invoker {
    private AbstractCommand command;

    public Invoker(AbstractCommand command) {
        this.command = command;
    }

    public void setCommand(AbstractCommand command) {
        this.command = command;
    }

    public void call() {
        System.out.println("调用者执行命令command...");
        command.execute();
    }

    public void add() {
        System.out.println("添加操作");
        command.addExecute();
    }
}

/**
 * 抽象命令
 */
abstract class AbstractCommand {
    public abstract void execute();

    public abstract void addExecute();

}

/**
 * 具体命令
 */
class ConcreteCommand extends AbstractCommand {
    private Receiver receiver;

    ConcreteCommand() {
        receiver = new Receiver();
    }

    @Override
    public void execute() {
        receiver.action();
    }

    @Override
    public void addExecute() {
        receiver.addExecute();
    }
}

//接收者
class Receiver {
    public void action() {
        System.out.println("接收者的action()方法被调用...");
    }

    public void addExecute() {
        System.out.println("接收者的add()方法被调用...");
    }
}