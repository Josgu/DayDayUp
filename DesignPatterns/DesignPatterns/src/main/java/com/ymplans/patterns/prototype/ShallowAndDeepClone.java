package com.ymplans.patterns.prototype;

/**
 * 深浅拷贝
 *
 * @author Jos
 */
public class ShallowAndDeepClone {

    public static void main(String[] args) {
        Head head = new Head();
        head.face = new Face();
        Head cloneHead = head.clone();
        System.out.println(head.equals(cloneHead));
        System.out.println(head.face.equals(cloneHead.face));
    }

}
class Head implements Cloneable{
    public Face face;

    @Override
    public Head clone() {
        try {
            Head head = (Head) super.clone();
            head.face = head.face.clone();
            return head;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

class Face implements Cloneable{

    @Override
    public Face clone() {
        try {
            return (Face) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}