/**
 * Created by cao_y on 2018/1/28.
 */

var prevQuestion = "";

var statusLabel = '#status a';
var searchBtn = '#search-btn';
var searchContent = '#search-content';
var loadIcon = "#loading";
var path = $('title').attr('path');
var answerArea = '.answer-area h2';

$(document).ready(function() {
  var dataStatus = $('#status').attr('status');
  if (dataStatus === 'false') {
    $(statusLabel).text("获取数据失败");
    $(statusLabel).css("color", "#c9302c");
  } else if (dataStatus === "true") {
    $(statusLabel).text("数据正常");
    $(statusLabel).css("color", "#419641");
    $(searchBtn).click(function(){
      onSearchClick();
    });

    $(searchContent).bind('input', function(){
      inputChange();
    });

    $(searchContent).bind('keypress', function() {
      if (event.keyCode == 13) {
        onSearchClick();
      }
    });
    console.log(path);
  }

  $(searchBtn).attr('disabled', true);
  $(loadIcon).hide();

  $('#reset-data').click(function() {
    resetData();
  });

  $('[data-toggle="tooltip"]').tooltip();

});

function onSearchClick() {
  $(searchBtn).attr('disabled', true);
  var question = $(searchContent).val();
  if (question !== prevQuestion) {
    var url = path + '/QA/ask?question=' + question;
    $(answerArea).empty();
    $(loadIcon).show();
    $.post(url, function(res){
      displayAnswer(res);
      $(loadIcon).hide();
      prevQuestion = question;
    });
  }
  $(searchBtn).attr('disabled', false);
}

function inputChange() {
  if ($(searchContent).val() === '') {
    $(searchBtn).attr('disabled', true);
  } else {
    $(searchBtn).attr('disabled', false);
  }
}

function displayAnswer(res) {
  console.log(res['answerString']);
  $(answerArea).html(res['answerString']);
}

function resetData() {
  var isReset = window.confirm('该操作将会清除现有数据并重新读取OWL文件，确定继续吗？');
  if (isReset) {
    var url = path + '/QA/resetData';
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
