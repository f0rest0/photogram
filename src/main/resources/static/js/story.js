/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */

// (0) 현재 로그인한 유저의 ID
let principalId = $("#principalId").val();
console.log("로그인한 유저 : ", principalId)
// 스크롤 페이징을 위한 변수생성

// (1) 스토리 로드하기
let page =0;

function storyLoad() {
	$.ajax({
		url:`/api/image?page=${page}`,
		dataType:"json"
	}).done(res=>{
		console.log(res);
		res.data.content.forEach((image)=>{
			let storyItem = getStoryItem(image);
			$("#storyList").append(storyItem);
		})
	}).fail(error=>{
		console.log("오류",error);
	});
}

storyLoad();

function getStoryItem(image) {
	let item=`<div class="story-list__item">
<div class="sl__item__header">
\t<div>
\t\t<img class="profile-image" src="/upload/${image.user.profileImageUrl}"
\t\t\tonerror="this.src='/images/person.jpeg'" />
\t</div>
\t<div>${image.user.name}</div>
</div>

<div class="sl__item__img">
\t<img src="/upload/${image.postImageUrl}" />
</div>

<div class="sl__item__contents">
\t<div class="sl__item__contents__icon">

<button>`;

	if (image.likeState) {
		item += `<i class="fas fa-heart active" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
	} else {
		item += `<i class="far fa-heart" id="storyLikeIcon-${image.id}" onclick="toggleLike(${image.id})"></i>`;
	}
	item += `
			
			</button>
\t</div>

\t<span class="like"><b id="storyLikeCount-${image.id}">${image.likeCount} </b>likes</span>

\t<div class="sl__item__contents__content">
\t\t<p>${image.caption}</p>
\t</div>

\t<div id="storyCommentList-${image.id}">`;
	image.comments.forEach((comment)=>{
		item +=`<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}">
<p>
		<b>${comment.user.username} :</b> ${comment.content}
</p>`;

		if (principalId == comment.user.id) {
			item +=
				`<button onclick="deleteComment(${comment.id})">
				    <i class="fas fa-times"></i>
                </button>`;
		}
		item += `
			</div>`;
	});

item +=`
\t</div>

\t<div class="sl__item__input">
\t\t<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${image.id}" />
\t\t<button type="button" onClick="addComment(${image.id})">게시</button>
\t</div>

</div>
</div>`;
	return item;
}

// (2) 스토리 스크롤 페이징하기
$(window).scroll(() => {
	let checkNum = $(window).scrollTop() - ($(document).height() - $(window).height());
	if (checkNum < 1 && checkNum > -1) {
		page++;
		storyLoad();
	}
});


// (3) 좋아요, 안좋아요
function toggleLike(imageId) {
	let likeIcon = $(`#storyLikeIcon-${imageId}`);

	if (likeIcon.hasClass("far")) {
		$.ajax({
			type: "post",
			url: `/api/image/${imageId}/likes`,
			dataType: "json"
		}).done(res => {

			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) + 1;
			$(`#storyLikeCount-${imageId}`).text(likeCount);

				likeIcon.addClass("fas");
				likeIcon.addClass("active");
				likeIcon.removeClass("far");
		}).fail(error => {
			console.log("오류", error);
		});
	} else {
		$.ajax({
			type: "delete",
			url: `/api/image/${imageId}/likes`,
			dataType: "json"
		}).done(res => {

			let likeCountStr = $(`#storyLikeCount-${imageId}`).text();
			let likeCount = Number(likeCountStr) - 1;
			$(`#storyLikeCount-${imageId}`).text(likeCount);

				likeIcon.removeClass("fas");
				likeIcon.removeClass("active");
				likeIcon.addClass("far");
		}).fail(error => {
			console.log("오류", error);
		});

	}
}

// (4) 댓글쓰기
function addComment(imageId) {

	let commentInput = $(`#storyCommentInput-${imageId}`);
	let commentList = $(`#storyCommentList-${imageId}`);

	let data = {
		imageId: imageId,
		content: commentInput.val()
	}

	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	$.ajax({
		type: "post",
		url: "/api/comment",
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		dataType: "json"
	}).done(res => {
		console.log("댓글쓰기 성공", res);

		let comment = res.data;

		let content = `
			<div class="sl__item__contents__comment" id="storyCommentItem-${comment.id}"> 
			    <p>
			        <b>${comment.user.name} :</b>
			        ${comment.content}
			    </p>
			    <button onclick="deleteComment(${comment.id})">
                <i class="fas fa-times"></i>
                </button>
			</div>
    	`;
		commentList.prepend(content);

	}).fail(error => {
		console.log("댓글쓰기 실패", error);
		alert("오류 : " + error.responseJSON.data.content);
	});

	commentInput.val("");
}

// (5) 댓글 삭제
function deleteComment(commentId) {
	$.ajax({
		type: "delete",
		url: `/api/comment/${commentId}`,
		dataType: "json"
	}).done(res => {
		console.log("댓글삭제 성공", res);
		$(`#storyCommentItem-${commentId}`).remove();
	}).fail(error => {
		console.log("댓글삭제 실패", error);
	});
}







