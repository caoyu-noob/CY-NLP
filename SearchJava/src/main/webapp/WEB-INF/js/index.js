/**
 * Created by cao_y on 2018/1/28.
 */
$(document).ready(function() {
  var dataStatus = $('#status').attr('status');
  if (dataStatus === 'false') {
    $('#status a').text("获取数据失败");
    $('#status a').css("color", "#c9302c");
  } else if (dataStatus === "true") {
    $('#status a').text("数据正常");
    $('#status a').css("color", "#419641");
  }
});

