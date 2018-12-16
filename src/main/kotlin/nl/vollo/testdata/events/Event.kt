package nl.vollo.testdata.events

interface Event<T> {
    val name: String;
    val body: T;
}
