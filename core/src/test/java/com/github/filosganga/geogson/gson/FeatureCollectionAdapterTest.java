package com.github.filosganga.geogson.gson;

import com.github.filosganga.geogson.gson.utils.FeatureUtils;
import com.github.filosganga.geogson.model.Feature;
import com.github.filosganga.geogson.model.FeatureCollection;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class FeatureCollectionAdapterTest {

  private Gson toTest;

  @Before
  public void initToTest() {
    toTest = new GsonBuilder().registerTypeAdapterFactory(new GeometryAdapterFactory()).create();
  }

  @Test
  public void shouldHandleEmptyFeatureCollection() {
    FeatureCollection collection = new FeatureCollection(Collections.<Feature>emptyList());
    FeatureCollection parsed = toTest.fromJson(toTest.toJson(collection), FeatureCollection.class);
    assertThat(parsed.features(), is(empty()));
  }

  @Test
  public void shouldHandleFeatureCollectionWithFeatures() {
    List<Feature> features = ImmutableList.of(
            FeatureUtils.featureWithId("test1"),
            FeatureUtils.featureWithId("test2")
    );
    FeatureCollection collection = new FeatureCollection(features);
    FeatureCollection parsed = toTest.fromJson(toTest.toJson(collection), FeatureCollection.class);
    assertThat(parsed.features(), hasSize(2));
    assertThat(parsed.features().get(0).id().get(), equalTo("test1"));
    assertThat(parsed.features().get(1).id().get(), equalTo("test2"));
  }

}
