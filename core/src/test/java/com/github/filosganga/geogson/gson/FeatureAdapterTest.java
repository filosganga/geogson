package com.github.filosganga.geogson.gson;

import com.github.filosganga.geogson.gson.utils.FeatureUtils;
import com.github.filosganga.geogson.model.*;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

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
    Feature feature = FeatureUtils.featureWithId("testid");
    Feature parsed = toTest.fromJson(toTest.toJson(feature), Feature.class);
    assertThat(parsed, equalTo(feature));
  }
  @Test
  public void shouldHandleFeaturesWithoutId() {
    Feature feature = FeatureUtils.featureWithGeometry(Point.from(12.3, 45.3));
    Feature parsed = toTest.fromJson(toTest.toJson(feature), Feature.class);
    assertThat(parsed, is(feature));
  }

  @Test
  public void shouldHandleGeometryAsProperty() {
    Point point = Point.from(12.3, 45.3);
    Feature feature = FeatureUtils.featureWithProperties(ImmutableMap.of("test", toTest.toJsonTree(point)));
    Feature parsed = toTest.fromJson(toTest.toJson(feature), Feature.class);
    assertThat(toTest.fromJson(parsed.properties().get("test"), Point.class), equalTo(point));
  }

}
