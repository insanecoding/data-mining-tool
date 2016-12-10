import React from 'react';
import ReactDOM from 'react-dom';
import App from './App';

describe("Jasmine test suite", function() {
    let a;

    it("and so is a spec", function() {
        a = true;

        expect(a).toBe(true);
    });

    it('renders without crashing', () => {
        const div = document.createElement('div');
        ReactDOM.render(<App />, div);
    });

    it("and can have a negative case", function() {
        expect(false).not.toBe(true);
    });

    it("works for simple literals and variables", function() {
        const a = 12;
        expect(a).toEqual(12);
    });

    it("should work for objects", function() {
        const foo = {
            a: 12,
            b: 34
        };
        const bar = {
            a: 12,
            b: 34
        };
        expect(foo).toEqual(bar);
    });

    it("The 'toMatch' matcher is for regular expressions", function() {
        const message = "foo bar baz";

        expect(message).toMatch(/bar/);
        expect(message).toMatch("bar");
        expect(message).not.toMatch(/quux/);
    });

    it("The 'toBeDefined' matcher compares against `undefined`", function() {
        const a = {
            foo: "foo"
        };

        expect(a.foo).toBeDefined();
        expect(a.bar).not.toBeDefined();
    });

    it("The 'toThrow' matcher is for testing if a function throws an exception", function() {
        const foo = function () {
            return 1 + 2;
        };
        const bar = function () {
            throw new Error("bla bla");
        };

        expect(foo).not.toThrow();
        expect(bar).toThrow();
    });
});