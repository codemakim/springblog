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
          location.href = '/post';
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