import 'package:android_metadata/android_metadata.dart';
import 'package:google_maps_flutter/google_maps_flutter.dart';
import 'package:google_maps_webservice/places.dart';
import 'package:location/location.dart' as loc;
import 'package:shoppingm8_fe/list/product/productCategory.dart';


class PlacesProvider {
  GoogleMapsPlaces _connector;

  PlacesProvider._(apiKey) {
    _connector = GoogleMapsPlaces(apiKey: apiKey);
  }

  static Future<String> _loadApiKey() async {
    Map<String, String> metadata = await AndroidMetadata.metaDataAsMap;
    return metadata["com.google.android.geo.API_KEY"];
  }

  static Future<PlacesProvider> build() async {
    return PlacesProvider._(await _loadApiKey());
  }

  Future<LatLng> getLocation() async {
    loc.Location location = new loc.Location();
    
    bool _serviceEnabled;
    loc.PermissionStatus _permissionGranted;

    _serviceEnabled = await location.serviceEnabled();
    if (!_serviceEnabled) {
      _serviceEnabled = await location.requestService();
      if (!_serviceEnabled) {
        return Future.value(null);
      }
    }

    _permissionGranted = await location.hasPermission();
    if (_permissionGranted == loc.PermissionStatus.denied) {
      _permissionGranted = await location.requestPermission();
      if (_permissionGranted != loc.PermissionStatus.granted) {
        return Future.value(null);
      }
    }
    loc.LocationData locationData = await location.getLocation();
    return LatLng(locationData.latitude, locationData.longitude);
  }

  Future<Set<Marker>> getPlacesByCategory(Set<ProductCategory> categories, location, {radius = 1000}) async {
    Set<String> types = {};
    for(ProductCategory category in categories) {
      types = types.union(ProductCategoryHepler.getGooglePlaceType(category));
    }

    if (location == null)
      return {};
    
    PlacesSearchResponse response =
        await _connector.searchNearbyWithRadius(new Location(location.latitude, location.longitude), radius, type: types.join("|"));
    if (!response.isOkay)
      return {};
    
    return response.results.map((r) => new Marker(
      markerId: new MarkerId(r.id),
      position: new LatLng(r.geometry.location.lat, r.geometry.location.lng))
    ).toSet();
  }
}