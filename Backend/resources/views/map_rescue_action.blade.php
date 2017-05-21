<div id="map" style="min-height: 600px;"></div>
<script>
  var map, heatmap;
  var center = {lat: 45.899288, lng: 15.947755};

  function initMap() {
    map = new google.maps.Map(document.getElementById('map'), {
        zoom: 13,
        center: center,
        mapTypeId: 'satellite'
    });

    heatmap = new google.maps.visualization.HeatmapLayer({
      data: getPoints(),
      map: map
    });
  }

  // Heatmap data: 500 Points
  function getPoints() {
    var points = [center];

    for(var i = 1; i < 50; ++i) {
        points.push({
            lat: points[i-1].lat + 0.0001,
            lng: points[i-1].lng + 0.0001
        });
    }

    var center2 = { lat: center.lat - 0.005, lng: center.lng - 0.005 };
    points.push(center2);

      for(var i = 1; i < 50; ++i) {
          points.push({
              lat: points[points.length - 1].lat - 0.00005,
              lng: points[points.length - 1].lng + 0.00005
          });
      }

      var center3 = { lat: center.lat, lng: center.lng - 0.005 };
      points.push(center3);

        for(var i = 1; i < 50; ++i) {
            points.push({
                lat: points[points.length - 1].lat,
                lng: points[points.length - 1].lng + 0.00005
            });
        }

      var center4 = { lat: center.lat - 0.005, lng: center.lng - 0.005 };
      points.push(center4);

        for(var i = 1; i < 50; ++i) {
            points.push({
                lat: points[points.length - 1].lat - 0.00010,
                lng: points[points.length - 1].lng - 0.00005
            });
        }

    return points.map(function(point) {
        return new google.maps.LatLng(point.lat, point.lng);
    });
  }


//  function initMap() {
//        map = new google.maps.Map(document.getElementById('map'), {
//          center: {lat: 44.629573, lng: 16.66626},
//          zoom: 7
//        });
//
//      $(markers).each(function() {
//        var lat_lon = this.coords.split(',');
//
//        var marker = new google.maps.Marker({
//            position: new google.maps.LatLng(lat_lon[0],lat_lon[1]),
//            map: map
//        });
//    });
//  }
</script>
<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDEISoFai3wymhBcqCMmuvFntjr59KyHm8&callback=initMap&libraries=visualization" async defer></script>