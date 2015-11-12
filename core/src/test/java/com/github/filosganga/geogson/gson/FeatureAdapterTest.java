package com.github.filosganga.geogson.gson;

import com.github.filosganga.geogson.model.*;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class FeatureAdapterTest {

  private Gson toTest;

  @Before
  public void initToTest() {
    toTest = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
  }

  @Test
  public void shouldHandleFeaturesWithId() {
    Feature feature = featureWithId("testid");
    Feature parsed = toTest.fromJson(toTest.toJson(feature), Feature.class);
    assertThat(parsed.id().get(), equalTo(feature.id().get()));
  }
  @Test
  public void shouldHandleFeaturesWithoutId() {
    Feature feature = featureWithGeometry(Point.from(12.3, 45.3));
    Feature parsed = toTest.fromJson(toTest.toJson(feature), Feature.class);
    assertThat(parsed.id().isPresent(), is(false));
  }

  @Test
  public void shouldHandleGeometryAsProperty() {
    Point point = Point.from(12.3, 45.3);
    Feature feature = featureWithProperties(ImmutableMap.of("test", toTest.toJsonTree(point)));
    Feature parsed = toTest.fromJson(toTest.toJson(feature), Feature.class);
    assertThat(toTest.fromJson(parsed.properties().get("test"), Point.class), equalTo(point));
  }

  private Feature featureWithId(String id) {
    return new Feature(Point.from(12.3, 45.3), ImmutableMap.<String, JsonElement>of(), Optional.of(id));
  }

  private Feature featureWithGeometry(Geometry<?> geometry) {
    return new Feature(geometry, ImmutableMap.<String, JsonElement>of(), Optional.<String>empty());
  }

  private Feature featureWithProperties(ImmutableMap<String, JsonElement> properties) {
    return new Feature(Point.from(12.3, 45.3), properties, Optional.<String>empty());
  }

}
