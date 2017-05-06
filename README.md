GeoJSON support for Gson
====================================
[![The Build Status](https://travis-ci.org/filosganga/geogson.png?branch=master)](https://travis-ci.org/filosganga/geogson)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/adc1d942b0c84f7893fd2fa5afdf0dfd)](https://www.codacy.com/app/me_62/geogson)
[![Download](https://api.bintray.com/packages/filosganga/maven/geogson/images/download.svg)](https://bintray.com/filosganga/maven/geogson/_latestVersion)

This library provide a minimal support to parse and write geo spatial entities
using the well known GeoJSON standard. I started write it because seems that
there are no extension to Gson available to support GeoJSON format, and it is
pretty common now across the new social application API.

If you are not familiar with the Gson library take a look at the [Gson project page](https://code.google.com/p/google-gson/).

If you are not familiar with the GeoJSON format, please take a look at the [The GeoJSON specification](http://geojson.org/geojson-spec.html).


## Quick Start
How to use the GeoGson in few easy steps.

### Add the Maven dependency to your pom.xml
Add the following statement to your pom.xml.

```xml
<dependency>
   <groupId>com.github.filosganga</groupId>
   <artifactId>geogson-core</artifactId>
   <version>1.1.97</version>
</dependency
```

### Register the TypeAdapterFactory with Gson
Use the GsonBuilder to register the ``GeometryAdapterFactory`` supplied.

```java
Gson gson = new GsonBuilder()
   .registerTypeAdapterFactory(new GeometryAdapterFactory())
   .create();
```

### Serialize and de-serialize with Gson
Now your Gson instance is able to parse and write any Geometry instance using
the GeoJSON format.

```java
String json = "{\"type\":\"Point\",\"coordinates\": [23.5,20.125]}";

Point point = gson.fromJson(json, Point.class);

String json = gson.toJson(point);

Geometry geometry = gson.fromJson(json); // It will be an instance of Point.
```

## Additional modules:
There are currently only one additional module for GeoGSON, the Java Topology
Suite (JTS) support.

If you are not familiar with the JTS library take a look at the [JTS Home Page](http://www.vividsolutions.com/jts/JTSHome.htm)

### JTS support
To enable the JTS support, you need declare the geogson-jts dependency and
register the ``JtsAdapterFactory`` as well.

In ``pom.xml``:

```xml
<dependency>
   <groupId>com.github.filosganga</groupId>
   <artifactId>geogson-jts</artifactId>
   <version>1.1.97</version>
</dependency
```

The Gson building code:

```java
Gson gson = new GsonBuilder()
   .registerTypeAdapterFactory(new JtsAdapterFactory())
   .registerTypeAdapterFactory(new GeometryAdapterFactory())
   .create();
```

Please note that you need the ``GeometryAdapterFactory`` anyway. The JTS
support is a thin layer on top of the native geometry domain.

You can optionally configure ``GeometryFactory`` as well. In this example all 
GeoJSON geometries are parsed as JTS geometries in WGS84 in centimeter precision:

```java
Gson gson = new GsonBuilder()
   .registerTypeAdapterFactory(new JtsAdapterFactory(
       new GeometryFactory(new PrecisionModel(100), 4326)))
   .registerTypeAdapterFactory(new GeometryAdapterFactory())
   .create();
```

## Enjoy beer with your friends, and buy me one!

