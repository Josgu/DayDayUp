package com.ymplans.patterns.observer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jos
 */
public class ObserverExample {

    public static void main(String[] args) {
        Subject concreteSubject = new ConcreteSubject();
        Observer concreteObserverOne = new ConcreteObserverOne();
        Observer concreteObserverTwo = new ConcreteObserverTwo();
        concreteSubject.registerObserver(concreteObserverOne);
        concreteSubject.registerObserver(concreteObserverTwo);
        concreteSubject.notifyObserver("Hi");
    }

}


interface Subject {
    void registerObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObserver(String message);
}

interface Observer {
    void update(String message);
}

class ConcreteSubject implements Subject{
    private final List<Observer> observers = new ArrayList<>();

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }
}

class ConcreteObserverOne implements Observer{

    @Override
    public void update(String message) {
        System.out.println("ConcreteObserverOne get " + message);
    }
}

class ConcreteObserverTwo implements Observer{

    @Override
    public void update(String message) {
        System.out.println("ConcreteObserverTwo get " + message);
    }
}
