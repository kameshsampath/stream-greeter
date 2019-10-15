package com.redhat.developers.demo;

import io.quarkus.test.junit.SubstrateTest;

@SubstrateTest
public class NativeGreeterResourceIT extends GreeterResourceTest {

    // Execute the same tests but in native mode.
}