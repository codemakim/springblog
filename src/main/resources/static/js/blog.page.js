var $Page = {
  init: function () {
    $Page.bindHandlers();
    $Page.summernoteInit();
  },

  /**
   * 이벤트를 바인딩하기 위한 함수입니다.
   */
  bindHandlers: function () {
    $(document).on('click', '.text-blog-title', function (e) {
      location.href = '/';
    });

    $(document).on('click', '.btn-post-delete', function (e) {
      console.log($(this).attr('data-index'));
      $Page.deletePost($(this).attr('data-index'));
    });

    $(document).on('click', '.btn-cancel-write', function (e) {
      window.history.back();
    });

    $(document).on('click', '.btn-modal-update-comment', function (e) {
      $Page.settingUpdateCommentForm($(this).data('postid'), $(this).data('commentid'));
    });

    $(document).on('hide.bs.modal', '#modalUpdateComment', function (e) {
      $(this).remove();
    });

    $(document).on('click', '.btn-comment-update', function (e) {
      $Page.updateComment();
    });

    $(document).on('click', '.btn-modal-delete-comment', function (e) {
      $Page.settingDeleteCommentForm($(this).data('postid'), $(this).data('commentid'));
    });

    $(document).on('click', '.btn-comment-delete', function (e) {
      $Page.deleteComment();
    });
  },

  /**
   * 포스트를 삭제하기 위한 함수입니다.
   * 인덱스를 넘겨받아 ajax 요청으로 해당하는 포스트를 삭제합니다.
   * 
   * @param {number} index 
   */
  deletePost: function (index) {
    $.ajax({
      url: '/post/delete/' + index,
      method: 'get',
      success: function (result) {
        if (result > 0) {
          location.href = '/';
        } else {
          setTimeout(function () {
            alert('포스트 삭제 작업 중 오류가 발생했습니다.');
          }, 500);
        }
      },
      error: function (xhr, textStatus, errorThrown) {
        console.log(errorThrown);
        if (xhr.status === 401) {
          location.href = '/member/login';
        } else {
          setTimeout(function () {
            alert('포스트 삭제에 실패했습니다.');
          }, 500);
        }
      }
    });
  },

  /**
   * 댓글 3수정을 위한 폼을 세팅하는 함수입니다.
   * 
   * @param {number} postId 
   * @param {number} commentId 
   */
  settingUpdateCommentForm: function (postId, commentId) {
    $.ajax({
      url: '/post/' + postId + '/comment/' + commentId + '/update',
      type: 'GET',
      success: function (result) {
        $('.replace-modal-update-comment').empty();
        setTimeout(function () { }, 500);
        $('.replace-modal-update-comment').append(result);
        $('#modalUpdateComment').modal('show');
      },
      error: function (xhr, textStatus, errorThrown) {
        alert('코멘트 수정 폼을 불러오는데 실패했습니다.');
        console.log(errorThrown);
      },
    });

    var $form = $('#updateCommentForm');
    if ($form.length < 1) {
      $form = $('<form/>').attr({ id: 'updateCommentForm', method: 'POST' });
      $form.appendTo('body');
    }
    $form.empty();
    $('<input/>').attr({ type: 'hidden', id: 'postIdUpdateForm', name: 'postId', value: postId, }).appendTo($form);
    $('<input/>').attr({ type: 'hidden', id: 'commentIdUpdateForm', name: 'id', value: commentId, }).appendTo($form);
    $('<input/>').attr({ type: 'hidden', name: 'password' }).appendTo($form);
    $('<input/>').attr({ type: 'hidden', name: 'content' }).appendTo($form);
    //비밀번호 창 초기화
    //수정 텍스트에리어의 내용을 해당하는 댓글 코멘트 내용을 가져와서 채워 넣기
  },

  /**
   * 댓글을 수정하기 위한 함수입니다
   */
  updateComment: function () {
    var $form = $('#updateCommentForm');

    var postId = $('input#postIdUpdateForm').val();
    var commentId = $('input#commentIdUpdateForm').val();

    $form.find('input[name=password]').val($('input#passwordUpdateComment').val());
    $form.find('[name=content]').val($('textarea#textareaUpdateComment').val());

    if ($form.length < 1) {
      alert('정상적인 경로로 접근하지 않았습니다.');
      return false;
    }
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
      url: '/post/' + postId + '/comment/' + commentId + '/update',
      type: 'POST',
      data: $form.serialize(),
      beforeSend: function (xhr) {
        xhr.setRequestHeader(header, token);
      },
      success: function (data) {
        window.location.reload();
      },
      error: function (xhr, textStatus, errorThrown) {
        alert('코멘트 수정에 실패했습니다.\n비밀번호를 올바르게 입력하셨나요?');
        console.log(errorThrown);
      },
    });
  },

  /**
   * 댓글 삭제를 위한 폼을 세팅하는 함수입니다.
   * 
   * @param {number} postId 
   * @param {number} commentId 
   */
  settingDeleteCommentForm: function (postId, commentId) {
    var $form = $('#deleteCommentForm');
    if ($form.length < 1) {
      $form = $('<form/>').attr({ id: 'deleteCommentForm', method: 'POST' });
      $form.appendTo('body');
    }
    $form.empty();
    $('<input/>').attr({ type: 'hidden', id: 'postIdDeleteForm', name: 'postId', value: postId, }).appendTo($form);
    $('<input/>').attr({ type: 'hidden', id: 'commentIdDeleteForm', name: 'id', value: commentId, }).appendTo($form);
  },

  /**
   * 댓글을 삭제하기 위한 함수입니다
   */
  deleteComment: function () {
    var $form = $('#deleteCommentForm');
    if ($form.length < 1) {
      alert('정상적인 댓글 삭제 기능 접근이 아닙니다.');
      return false;
    }
    var password = $.trim($('#passwordDeleteComment').val());
    if (password.length < 1 || password == '') {
      alert('비밀번호를 입력하세요');
      return false;
    }
    $('#passwordDeleteComment').appendTo($form);

    console.log($form.serialize());

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
      url: '/post/' + $('#postIdDeleteForm').val() + '/comment/' + $('#commentIdDeleteForm').val() + '/delete',
      type: 'POST',
      data: $form.serialize(),
      beforeSend: function (xhr) {
        xhr.setRequestHeader(header, token);
      },
      success: function (data) {
        window.location.reload();
      },
      error: function (xhr, textStatus, errorThrown) {
        alert('코멘트 삭제에 실패했습니다.\n비밀번호를 올바르게 입력하셨나요?');
        console.log(errorThrown);
      },
    });
  },

  /**
   * 섬머노트를 초기화하기 위한 함수입니다.
   */
  summernoteInit: function () {
    if ($('#summernote').length > 0) {
      $('#summernote').summernote({
        height: 300,
        minHeight: null,
        maxHeight: null,
        focus: true,
        callbacks: {
          onImageUpload: function (files, editor, welEditable) {
            for (var i = files.length - 1; i >= 0; i--) {
              $Page.sendFile(files[i], this);
            }
          }
        },
      });
    }
  },

  /**
   * 파일과 엘리먼트를 입력받습니다. 파일 업로드를 완료한 뒤, 파일 다운로드 url을
   * 이미지 태그에 지정한 후, 입력받은 엘리먼트에 append합니다.
   * 
   * @param {MultipartFile} file 
   * @param {HTMLElement} el 
   */
  sendFile: function (file, el) {
    var formData = new FormData(document.getElementById('uplaodForm'));
    formData.append('file', file);
    $.ajax({
      data: formData,
      type: 'post',
      url: '/image',
      cache: false,
      contentType: false,
      encType: 'multipart/form-data',
      processData: false,
      success: function (url) {
        $(el).summernote('editor.insertImage', url);
        $('#imageBoard > ul').append('<li><img src="' + url + '" width="480" height="auto"/></li>');
      }
    });
    formData.delete('file');
  },


};