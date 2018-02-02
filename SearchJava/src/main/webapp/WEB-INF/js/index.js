/**
 * Created by cao_y on 2018/1/28.
 */

var prevQuestion = "";

$(document).ready(function() {
  var dataStatus = $('#status').attr('status');
  if (dataStatus === 'false') {
    $('#status a').text("获取数据失败");
    $('#status a').css("color", "#c9302c");
  } else if (dataStatus === "true") {
    $('#status a').text("数据正常");
    $('#status a').css("color", "#419641");
  }
  $('#search-btn').attr('disabled', true);
  $('#search-btn').click(function(){
    onSearchClick();
  });
  $('#search-content').bind('input', function(){
    inputChange();
  });
  $('#search-content').bind('keypress', function() {
    if (event.keyCode == 13) {
      onSearchClick();
    }
  });
  $('#reset-data').click(function() {
    resetData();
  })
});

function onSearchClick() {
  $('#search-btn').attr('disabled', true);
  var question = $('#search-content').val();
  if (question !== prevQuestion) {
    var url = '/QA/ask?question=' + question;
    $.post(url, function(res){
      displayAnswer(res);
      prevQuestion = question;
    });
  }
  $('#search-btn').attr('disabled', false);
}

function inputChange() {
  if ($('#search-content').val() === '') {
    $('#search-btn').attr('disabled', true);
  } else {
    $('#search-btn').attr('disabled', false);
  }
}

function displayAnswer(res) {
  console.log(res['answerString']);
  $('.answer-area h2').html(res['answerString']);
}

function resetData() {
  var isReset = window.confirm('该操作将会清除现有数据并重新读取OWL文件，确定继续吗？');
  if (isReset) {
    var url = '/QA/resetData';
    $.post(url, function(res){
      var isSuccess = res['isSuccess'];
      if (isSuccess === 'false') {
        window.alert(res['message']);
      } else {
        window.alert(res['message']);
        location.reload();
      }
    });
  }
}
