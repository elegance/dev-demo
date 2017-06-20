package org.orh.pattern;

import java.awt.Color;
/**
 * State 状态模式
 */
public class StatePattern {
    public static void main(String[] args) {
        Context ctx = new Context();
        ctx.setState(Color.red);
        ctx.push();
        ctx.push();
        ctx.pull();
        System.out.println(ctx.getState());
        
        StateContext sctx = new StateContext();
        sctx.setState(new RedState());
        sctx.push();
        sctx.push();
        sctx.pull();
        System.out.println(sctx.state.getcolor());
    }

    static class Context {
        private Color state = null;

        public void push() {
            if (state == Color.red)
                state = Color.blue;
            else if (state == Color.blue)
                state = Color.green;
            else if (state == Color.green)
                state = Color.red;
        }

        public void pull() {
            if (state == Color.blue)
                state = Color.red;
            else if (state == Color.green)
                state = Color.blue;
            else if (state == Color.red)
                state = Color.green;
        }

        public Color getState() {
            return state;
        }

        public void setState(Color state) {
            this.state = state;
        }
    }
    static class StateContext {
        private State state = null;

        public void setState(State state) {
            this.state = state;
        }

        public void push() {
            state.handlepush(this);
        }

        public void pull() {
            state.handlepull(this);
        }
    }

    static abstract class State {
        public abstract void handlepush(StateContext c);

        public abstract void handlepull(StateContext c);

        public abstract Color getcolor();
    }
    static class RedState extends State {

        @Override
        public void handlepush(StateContext c) {
            c.setState(new BlueState());
        }

        @Override
        public void handlepull(StateContext c) {
            c.setState(new GreenState());
        }

        @Override
        public Color getcolor() {
            return Color.red;
        }

    }
    static class BlueState extends State {

        @Override
        public void handlepush(StateContext c) {
            c.setState(new GreenState());
        }

        @Override
        public void handlepull(StateContext c) {
            c.setState(new RedState());
        }

        @Override
        public Color getcolor() {
            return Color.blue;
        }

    }
    static class GreenState extends State {

        @Override
        public void handlepush(StateContext c) {
            c.setState(new RedState());
        }

        @Override
        public void handlepull(StateContext c) {
            c.setState(new BlueState());
        }

        @Override
        public Color getcolor() {
            return Color.green;
        }

    }
}
