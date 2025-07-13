/*
 * Apollo - Sticky Full Width HTML5 Audio Player - v3.6.2
 *
 * Copyright 2017-2025, LambertGroup
 *
 */

(function($) {
	"use strict";

	//vars
	var val = navigator.userAgent.toLowerCase();

	//functions
	function supports_mp3_audio(current_obj) {
			  var a = document.getElementById(current_obj.audioID);
			  return !!(a.canPlayType && a.canPlayType('audio/mpeg;').replace(/no/, ''));
	}

	function detectBrowserAndAudio(current_obj,options,audio7_html5_thumbsHolder,audio7_html5_container) {
				//activate current
				//$(current_obj.thumbsHolder_Thumbs[current_obj.current_img_no]).addClass('thumbsHolder_ThumbON');
				$(current_obj.thumbsHolder_Thumbs[current_obj.current_img_no]).css({
					"background":options.playlistRecordBgOnColor,
					"border-bottom-color":options.playlistRecordBottomBorderOnColor,
					"color":options.playlistRecordTextOnColor
				});

				//auto scroll carousel if needed
				if (!current_obj.is_very_first) {
					carouselScroll(-1,current_obj,options,audio7_html5_thumbsHolder);
				}
				//alert ("detect: "+current_obj.origID+'  ---  '+current_obj.playlist_arr[current_obj.origID]['sources_mp3']);
				var currentAudio=current_obj.playlist_arr[current_obj.origID]['sources_mp3'];

				//alert (val);
				if (val.indexOf("opera") != -1 || val.indexOf("firefox") != -1  || val.indexOf("mozzila") != -1) {
					currentAudio=current_obj.playlist_arr[current_obj.origID]['sources_ogg'];
					if (supports_mp3_audio(current_obj)!='') {
						currentAudio=current_obj.playlist_arr[current_obj.origID]['sources_mp3'];
					}
				}

				if (val.indexOf("chrome") != -1 || val.indexOf("msie") != -1 || val.indexOf("safari") != -1) {
					currentAudio=current_obj.playlist_arr[current_obj.origID]['sources_mp3'];
					if (val.indexOf("opr") != -1) {
						currentAudio=current_obj.playlist_arr[current_obj.current_img_no]['sources_ogg'];
						if (supports_mp3_audio(current_obj)!='') {
							currentAudio=current_obj.playlist_arr[current_obj.origID]['sources_mp3'];
						}
					}
				}

				if (val.indexOf("android") != -1)
					currentAudio=current_obj.playlist_arr[current_obj.origID]['sources_mp3'];

				//if (val.match(/(iPad)|(iPhone)|(iPod)|(webOS)/i))
				if (val.indexOf("ipad") != -1 || val.indexOf("iphone") != -1 || val.indexOf("ipod") != -1 || val.indexOf("webos") != -1)
					currentAudio=current_obj.playlist_arr[current_obj.origID]['sources_mp3'];

				//alert (currentAudio+ '  --  ' +val);
				return currentAudio;
			};



			function changeSrc(current_obj,options,audio7_html5_thumbsHolder,audio7_html5_container,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_rewind_btn) {
					if (current_obj.current_img_no!=undefined) { /* I have a valid playlist element */
										if (options.shuffle && current_obj.is_very_first) {
												// nothing
												//alert ('nothing:  '+options.shuffle+' && '+current_obj.is_very_first);
										} else {
													//alert ('something:  '+options.shuffle+' && '+current_obj.is_very_first);
														current_obj.totalTime = 'Infinity';
														//seekbar init
														if (options.isSliderInitialized) {
															audio7_html5_Audio_seek.slider("destroy");
															options.isSliderInitialized=false;
														}
														if (options.isProgressInitialized) {
															audio7_html5_Audio_buffer.progressbar("destroy");
															options.isProgressInitialized=false;
														}
														//audio7_html5_Audio.unbind('progress');
														current_obj.is_changeSrc=true;
														current_obj.is_buffer_complete=false;

														//current_obj.totalTimeInterval='Infinity';

														//audio7_html5_Title init
													/*audio7_html5_ximage.css({
														'left':current_obj.timerLeftPos+'px',
														'top':current_obj.timerTopPos+audio7_html5_Audio_timer_a.height()+current_obj.constantDistance+'px'
													});*/
														audio7_html5_Title.width(current_obj.titleWidth);
														audio7_html5_Author.width(current_obj.titleWidth);
														audio7_html5_Audio_buffer.css({'background':options.bufferEmptyColor});

														//.each(function(){ alert ("aaaa"); });

														//current_obj.origID=;

														current_obj.curSongText='';
														if (options.showTitle && current_obj.playlist_arr[current_obj.origID]['title']!=null && current_obj.playlist_arr[current_obj.origID]['title']!='') {
											            	current_obj.curSongText+=current_obj.playlist_arr[current_obj.origID]['title'];
											            }
														current_obj.isAuthorTitleInsideScrolling=false;
														current_obj.authorTitleInsideWait=0;
														audio7_html5_TitleInside.stop();
														audio7_html5_TitleInside.css({'margin-left':0});
														audio7_html5_TitleInside.html(current_obj.curSongText);

														if (options.showAuthor && current_obj.playlist_arr[current_obj.origID]['author']!=null && current_obj.playlist_arr[current_obj.origID]['author']!='') {
															audio7_html5_Author.html(current_obj.playlist_arr[current_obj.origID]['author']);
															if (current_obj.playlist_arr[current_obj.origID]['authorlink']!=null && current_obj.playlist_arr[current_obj.origID]['authorlink']!='') {
																		audio7_html5_Author.html('<a href="'+current_obj.playlist_arr[current_obj.origID]['authorlink']+'" target="'+current_obj.playlist_arr[current_obj.origID]['authorlinktarget']+'" style="color:'+options.songAuthorColor+';">'+current_obj.playlist_arr[current_obj.origID]['author']+'</a>');
															}
														}

														audio7_html5_ximage.html('<img src="'+current_obj.playlist_arr[current_obj.origID]['image']+'" width="149">');

														if (!current_obj.curSongText) {
															audio7_html5_Title.css({
																'display':'none',
																'width':0,
																'height':0,
																'padding':0,
																'margin':0
															});
														}


														//audio7_html5_Audio.type='audio/ogg; codecs="vorbis"';
														var the_file=detectBrowserAndAudio(current_obj,options,audio7_html5_thumbsHolder,audio7_html5_container);
														document.getElementById(current_obj.audioID).src=the_file;
														document.getElementById(current_obj.audioID).load();

														if (options.googleTrakingOn) {
															ga('send', 'event', 'Audio Files', 'Play', 'Title: '+current_obj.playlist_arr[current_obj.origID]['title']+'  ---  File: '+the_file);
														}

														//alert (audio7_html5_Audio.type );


														if (val.indexOf("android") != -1) {
															//nothing
														} else if ((val.indexOf("ipad") != -1 || val.indexOf("iphone") != -1 || val.indexOf("ipod") != -1 || val.indexOf("webos") != -1) && current_obj.is_very_first) {
															//nothing
														} else {
															if (options.autoPlay) {
																cancelAll(options,audio7_html5_container);
																document.getElementById(current_obj.audioID).play();
																//audio7_html5_play_btn.click();
																audio7_html5_play_btn.addClass('AudioPause');
																manageCss3Animations(options,current_obj,audio7_html5_container,true);
															} else {
																audio7_html5_play_btn.removeClass('AudioPause');
																manageCss3Animations(options,current_obj,audio7_html5_container,false);
															}
														}
										}
					} else {
						audio7_html5_rewind_btn.click();
					}

			};




			function FormatTime(seconds){
				var m=Math.floor(seconds/60)<10?"0"+Math.floor(seconds/60):Math.floor(seconds/60);
				var s=Math.floor(seconds-(m*60))<10?"0"+Math.floor(seconds-(m*60)):Math.floor(seconds-(m*60));
				return m+":"+s;
			};





			function generate_seekBar(current_obj,options,audio7_html5_container,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_play_btn,audio7_html5_Audio) {
				//alert ("gen: "+document.getElementById(current_obj.audioID).readyState);
					current_obj.is_changeSrc=false;
					if (current_obj.is_very_first)
						current_obj.is_very_first=false;
					//initialize the seebar
					//alert (audio7_html5_Audio_timer_b.width());
					audio7_html5_Audio_buffer.width(current_obj.bufferWidth);


					current_obj.bufferTopPos=current_obj.timerTopPos+parseInt((audio7_html5_Audio_timer_a.height()-audio7_html5_Audio_buffer.height())/2,10)+1;
					current_obj.bufferLeftPos=parseInt(current_obj.timerLeftPos+audio7_html5_Audio_timer_a.width()+11);
					audio7_html5_Audio_buffer.css({
						'top':current_obj.bufferTopPos+'px',
						'left':current_obj.bufferLeftPos+'px'
						//'left':parseInt(current_obj.timerLeftPos+audio7_html5_Audio_timer_a.width()+current_obj.seekBarLeftRightSpacing)+'px'
					});
					audio7_html5_Audio_seek.width(audio7_html5_Audio_buffer.width());
					audio7_html5_Audio_seek.css({
						'top':current_obj.bufferTopPos+'px',
						'left':current_obj.bufferLeftPos+'px'
						//'left':parseInt(current_obj.timerLeftPos+audio7_html5_Audio_timer_a.width()+current_obj.seekBarLeftRightSpacing)+'px'
					});

					audio7_html5_Audio_seek.slider({
						value: 0,
						step: 0.01,
						orientation: "horizontal",
						range: "min",
						max: current_obj.totalTime,
						//animate: true,
						slide: function(){
							current_obj.is_seeking = true;
						},
						stop:function(e,ui){
							current_obj.is_seeking = false;
							document.getElementById(current_obj.audioID).currentTime=ui.value;
							/*if(document.getElementById(current_obj.audioID).paused != false) {
								document.getElementById(current_obj.audioID).play();
								audio7_html5_play_btn.addClass('AudioPause');
							}*/

						},
						create: function( e, ui ) {
							options.isSliderInitialized=true;
						}
					});
					$(".ui-slider-range",audio7_html5_Audio_seek).css({'background':options.seekbarColor});



					var bufferedTime=0;
					audio7_html5_Audio_buffer.progressbar({
						value: bufferedTime,
						complete: function(){
							current_obj.is_buffer_complete=true;
						},
						create: function( e, ui ) {
							options.isProgressInitialized=true;
						}
					});
					$(".ui-widget-header",audio7_html5_Audio_buffer).css({'background':options.bufferFullColor});



			};




			function seekUpdate(current_obj,options,audio7_html5_container,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_play_btn,audio7_html5_Audio,audio7_html5_Title,audio7_html5_TitleInside) {
				if (!current_obj.isAuthorTitleInsideScrolling && current_obj.authorTitleInsideWait>=5 && audio7_html5_TitleInside.width()>current_obj.titleWidth) {
					current_obj.isAuthorTitleInsideScrolling=true;
					current_obj.authorTitleInsideWait=0;
					audio7_html5_TitleInside.html(current_obj.curSongText+" **** "+current_obj.curSongText+" **** "+current_obj.curSongText+" **** "+current_obj.curSongText+" **** "+current_obj.curSongText+" **** ");
					audio7_html5_TitleInside.css({'margin-left':0});
					audio7_html5_TitleInside.stop().animate({
							'margin-left':(options.playerWidth-audio7_html5_TitleInside.width())+'px'
					 }, parseInt((audio7_html5_TitleInside.width()-options.playerWidth)*10000/150,10), 'linear', function() {
							// Animation complete.
							  current_obj.isAuthorTitleInsideScrolling=false;
					});
				} else if (!current_obj.isAuthorTitleInsideScrolling && audio7_html5_TitleInside.width()>current_obj.titleWidth) {
					current_obj.authorTitleInsideWait++;
				}

				//update time
				var curTime = document.getElementById(current_obj.audioID).currentTime;
				var bufferedTime=0;
				if (current_obj.is_changeSrc && !isNaN(current_obj.totalTime) && current_obj.totalTime!='Infinity') {
					//alert (current_obj.totalTime);
					generate_seekBar(current_obj,options,audio7_html5_container,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_play_btn,audio7_html5_Audio);
					if (val.indexOf("android") != -1) {
						if (options.autoPlay) {
							document.getElementById(current_obj.audioID).play();
							//audio7_html5_play_btn.click();
							audio7_html5_play_btn.addClass('AudioPause');
						} else {
							audio7_html5_play_btn.removeClass('AudioPause');
						}
					}
				}

				if (document.getElementById(current_obj.audioID).paused) {
							manageCss3Animations(options,current_obj,audio7_html5_container,false);
				}


						//update seekbar
						if(!current_obj.is_seeking && options.isSliderInitialized)
							audio7_html5_Audio_seek.slider('value', curTime);

						//the buffer
						if (val.indexOf("android") != -1) {
							//fix duration android 4 start
							if (current_obj.totalTime!=document.getElementById(current_obj.audioID).duration && document.getElementById(current_obj.audioID).duration>0) {
								current_obj.totalTime=document.getElementById(current_obj.audioID).duration;
								//seekbar init
								if (options.isSliderInitialized) {
									audio7_html5_Audio_seek.slider("destroy");
									options.isSliderInitialized=false;
								}
								if (options.isProgressInitialized) {
									audio7_html5_Audio_buffer.progressbar("destroy");
									options.isProgressInitialized=false;
								}
								generate_seekBar(current_obj,options,audio7_html5_container,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_play_btn,audio7_html5_Audio);
							}
							//fix duration android 4 start

							audio7_html5_Audio_buffer.css({'background':options.bufferFullColor});
							if (!isNaN(current_obj.totalTime) && current_obj.totalTime!='Infinity') {
								audio7_html5_Audio_timer_a.text(FormatTime(curTime));
								audio7_html5_Audio_timer_b.text(FormatTime(current_obj.totalTime));
							} else {
								audio7_html5_Audio_timer_a.text('00:00');
								audio7_html5_Audio_timer_b.text(FormatTime(0));
							}
						} else {
								if (document.getElementById(current_obj.audioID).buffered.length) {
									bufferedTime = document.getElementById(current_obj.audioID).buffered.end(document.getElementById(current_obj.audioID).buffered.length-1);
									//alert (current_obj.totalTime + ' > '+bufferedTime);
									if (bufferedTime>0 && !current_obj.is_buffer_complete && !isNaN(current_obj.totalTime) && current_obj.totalTime!='Infinity' && options.isProgressInitialized) {
										audio7_html5_Audio_buffer.progressbar({ value: bufferedTime*100/current_obj.totalTime });
										//alert (bufferedTime+' -- '+options.playerWidth);
									}
								}
								audio7_html5_Audio_timer_a.text(FormatTime(curTime));
								//audio7_html5_Audio_timer_b.text(FormatTime(bufferedTime));
								//duration fix
								if (!isNaN(current_obj.totalTime) && current_obj.totalTime!='Infinity') {
									audio7_html5_Audio_timer_b.text(FormatTime(current_obj.totalTime));
								} else {
									audio7_html5_Audio_timer_b.text(FormatTime(bufferedTime));
								}
						}
						setCookie(options,'cookie_timePlayed', curTime);
				/*} else {
					audio7_html5_Audio_timer.text('00:00 / '+FormatTime(0));
				}*/



			};


			function endAudioHandler(current_obj,options,audio7_html5_container,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_next_btn,audio7_html5_Audio) {
		        if (options.loop) {
					audio7_html5_next_btn.click();
		        }
		    }


		//playlist scroll
		function carouselScroll(direction,current_obj,options,audio7_html5_thumbsHolder) {
				if (current_obj.selectedCateg_total_images>options.numberOfThumbsPerScreen) {
						var MAX_TOP=(current_obj.thumbsHolder_ThumbHeight+1)*(current_obj.selectedCateg_total_images-options.numberOfThumbsPerScreen);
						var new_top;
						//alert (current_obj.audio7_html5_sliderVertical.slider( "option", "animate" ));
						audio7_html5_thumbsHolder.stop(true,true);
						//page scroll enabled
						$('html, body')
		            // Needed to remove previously bound handlers
		            .off('touchstart touchmove')
		            .on('touchstart touchmove', function (e) {
		                e.preventDefault();
		            });
						//page scroll enabled

						if (direction!=-1 && !current_obj.isCarouselScrolling) {
							new_top=((direction<=2)?(-1)*MAX_TOP:parseInt(MAX_TOP*(direction-100)/100,10));
							if (new_top>=1) {
								new_top=0;
						    }
							if (new_top<=0) {
								current_obj.isCarouselScrolling=true;
								audio7_html5_thumbsHolder.animate({
									//opacity: 1,
									//top:parseInt(MAX_TOP*(direction-100)/100,10)+'px'
									top:new_top+'px'
								}, 600, 'easeOutQuad', function() {
									// Animation complete.
									  current_obj.isCarouselScrolling=false;
										//page scroll enabled
										$('html, body')
			            		.off('touchstart touchmove')
			            		.on('touchstart touchmove', function (e) {});
										//page scroll enabled
								});
							}
						} else if (!current_obj.isCarouselScrolling && current_obj.selectedCateg_total_images>options.numberOfThumbsPerScreen) {
							current_obj.isCarouselScrolling=true;
							//audio7_html5_thumbsHolder.css('opacity','0.5');
							new_top=(-1)*parseInt((current_obj.thumbsHolder_ThumbHeight+1)*current_obj.current_img_no,10);
							if( Math.abs(new_top) > MAX_TOP ){ new_top = (-1)*MAX_TOP; }
							if (current_obj.selectedCateg_total_images>options.numberOfThumbsPerScreen && options.showPlaylist) {
								current_obj.audio7_html5_sliderVertical.slider( "value" , 100 + parseInt( new_top * 100 / MAX_TOP ) );
							}
							audio7_html5_thumbsHolder.animate({
							    //opacity: 1,
							    top:new_top+'px'
							  }, 500, 'easeOutCubic', function() {
							    // Animation complete.
								  current_obj.isCarouselScrolling=false;
									//page scroll enabled
									$('html, body')
										.off('touchstart touchmove')
										.on('touchstart touchmove', function (e) {});
									//page scroll enabled
							});
						}
				}
			};


			function arrangePlayerElements(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_innerSelectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_vinyl_record_on,audio7_html5_vinyl_record_off,audio7_html5_the_wrapper,audio7_html5_popup_btn,audio7_html5_the_bars,audio7_html5_volumeMute_btn,audio7_html5_volumeSlider,audio7_html5_shuffle_btn,audio7_html5_download_btn,audio7_html5_showHidePlaylist_btn,audio7_html5_lyrics_btn,audio7_html5_facebook_btn,audio7_html5_twitter_btn,audio7_html5_rewind_btn,audio7_html5_prev_btn,audio7_html5_next_btn,audio7_html5_buy_btn,audio7_html5_clearFavoritesBut) {
					//the image
					/*current_obj.imageTopPos=parseInt(audio7_html5_ximage.css('top').substring(0, audio7_html5_ximage.css('top').length-2),10);
					current_obj.imageLeftPos=parseInt(audio7_html5_ximage.css('left').substring(0, audio7_html5_ximage.css('left').length-2),10);*/
					current_obj.imageTopPos=-27;
					current_obj.imageLeftPos=current_obj.constantDistance;
					if (options.showVinylRecord) {
							audio7_html5_ximage.css({
								'display':'none'
							});
							audio7_html5_vinyl_record_on.css({
								'top':current_obj.imageTopPos+'px',
								'left':current_obj.imageLeftPos+'px'
							});
							audio7_html5_vinyl_record_off.css({
								'top':current_obj.imageTopPos+'px',
								'left':current_obj.imageLeftPos+'px'
							});
					} else {
							audio7_html5_vinyl_record_on.css({
								'visibility':'hidden',
								'display':'none'
							});
							audio7_html5_vinyl_record_off.css({
								'visibility':'hidden',
								'display':'none'
							});
							audio7_html5_ximage.css({
								'top':current_obj.imageTopPos+'px',
								'left':current_obj.imageLeftPos+'px'
							});
					}


					//HIDE THE BARS
					if (options.playerWidth<current_obj.stickyFixedPlayerWidth) {
						  audio7_html5_the_bars.css ({
							  'display':'none'
						  });
					} else {
						  audio7_html5_the_bars.css ({
							  'display':'block'
						  });
					}

					//HIDE THE IMAGE
					if (options.playerWidth<645) {
						  if (options.showVinylRecord) {
								  audio7_html5_vinyl_record_on.css({
									  'visibility':'hidden',
									  'display':'none'
								  });
								  audio7_html5_vinyl_record_off.css({
									  'visibility':'hidden',
									  'display':'none'
								  });
						  } else {
								  audio7_html5_ximage.css({
									  'display':'none'
								  });
						  }
						  ////current_obj.playLeftPos=current_obj.imageLeftPos; // it is also defined in arrangePlayerElements() function
					} else {
						  if (options.showVinylRecord) {
							  	if (!document.getElementById(current_obj.audioID).paused) {
										  audio7_html5_vinyl_record_on.css({
											  'visibility':'visible',
											  'display':'block'
										  });
										  audio7_html5_vinyl_record_off.css({
											  'visibility':'hidden',
											  'display':'none'
										  });
								} else {
										  audio7_html5_vinyl_record_on.css({
											  'visibility':'hidden',
											  'display':'none'
										  });
										  audio7_html5_vinyl_record_off.css({
											  'visibility':'visible',
											  'display':'block'
										  });
								}
						  } else {
								  audio7_html5_ximage.css({
									  'display':'block'
								  });
						  }
						  /////current_obj.playLeftPos=current_obj.imageLeftPos+audio7_html5_ximage.width()+24; // it is also defined in arrangePlayerElements() function
					}

				   //HIDE THE TITLE & AUTHOR
				   if (options.playerWidth<460) {
						  audio7_html5_Author.css({
							  'display':'none'
						  });

						  audio7_html5_Title.css({
							  'display':'none'
						  });
				   } else {
						  if (options.showAuthor) {
							  audio7_html5_Author.css({
								  'display':'block'
							  });
						  }
						  if (options.showTitle) {
							  audio7_html5_Title.css({
								  'display':'block'
							  });
						  }
				   }

				   //HIDE THE BUFFER & TIMER
				   if (options.playerWidth<395) {
						  audio7_html5_Audio_seek.css({
							  'display':'none'
						  });

						  audio7_html5_Audio_buffer.css({
							  'display':'none'
						  });

						  audio7_html5_Audio_timer_a.css({
							  'margin-left':'7px'
						  });

						  audio7_html5_Audio_timer_b.css({
							  'display':'none'
						  });
				   } else {
						  audio7_html5_Audio_seek.css({
							  'display':'block'
						  });

						  audio7_html5_Audio_buffer.css({
							  'display':'block'
						  });

						  audio7_html5_Audio_timer_a.css({
							  'margin-left':'0px'
						  });

						  audio7_html5_Audio_timer_b.css({
							  'display':'block'
						  });
				   }

				   //HIDE VOLUME
				   if (options.playerWidth<345) {
						  audio7_html5_volumeMute_btn.css({
							  'display':'none'
						  });

						  audio7_html5_volumeSlider.css({
							  'display':'none'
						  });
				   } else {
						  audio7_html5_volumeMute_btn.css({
							  'display':'block'
						  });

						  audio7_html5_volumeSlider.css({
							  'display':'block'
						  });
				   }


					// set player height
					current_obj.audioPlayerHeight=audio7_html5_ximage.height()+current_obj.imageTopPos;
					audio7_html5_container.height(current_obj.audioPlayerHeight);

					//play button
					current_obj.playTopPos=15;
					if (options.playerWidth<645) {
						current_obj.playLeftPos=current_obj.imageLeftPos;
					} else {
						current_obj.playLeftPos=current_obj.imageLeftPos+audio7_html5_ximage.width()+24;
					}

					audio7_html5_play_btn.css({
						'top':current_obj.playTopPos+'px',
						'left':current_obj.playLeftPos+'px'
					});


					current_obj.bufferWidth=options.playerWidth-(current_obj.playLeftPos+5)-2*audio7_html5_Audio_timer_a.width()-2*current_obj.seekBarLeftRightSpacing-2*options.playerPadding-260-20;
					//author & title
					audio7_html5_Title.css({'color':options.songTitleColor});
					audio7_html5_Author.css({'color':options.songAuthorColor});
					if (audio7_html5_the_bars.css('display')=='none') {
						current_obj.titleWidth=current_obj.bufferWidth+audio7_html5_Audio_timer_a.width()+2*current_obj.seekBarLeftRightSpacing+current_obj.constantDistance-audio7_html5_play_btn.width();
						//alert ("zzz: "+current_obj.titleWidth+'  --  '+current_obj.bufferWidth+'  --  '+audio7_html5_play_btn.width());
					} else {
						//current_obj.titleWidth=240;
						current_obj.titleWidth=current_obj.bufferWidth+2*audio7_html5_Audio_timer_a.width()+2*current_obj.seekBarLeftRightSpacing-audio7_html5_play_btn.width()-audio7_html5_the_bars.width()-(current_obj.constantDistance+25+20);
					}
					audio7_html5_Title.width(current_obj.titleWidth);
					audio7_html5_Author.width(current_obj.titleWidth);

					current_obj.authorTopPos=20;
					current_obj.authorLeftPos=current_obj.playLeftPos+audio7_html5_play_btn.width()+current_obj.constantDistance;

					current_obj.titleTopPos=current_obj.authorTopPos+audio7_html5_Author.height()+10;
					current_obj.titleLeftPos=current_obj.authorLeftPos;

					audio7_html5_Author.css({
						'top':current_obj.authorTopPos+'px',
						'left':current_obj.authorLeftPos+'px'
					});

					audio7_html5_Title.css({
						'top':current_obj.titleTopPos+'px',
						'left':current_obj.titleLeftPos+'px'
					});


					//timer
					current_obj.timerTopPos=current_obj.playTopPos+audio7_html5_play_btn.height()+current_obj.constantDistance;
					current_obj.timerLeftPos=current_obj.playLeftPos+5;
					audio7_html5_Audio_timer_a.css({
						'color':options.timerColor,
						'top':current_obj.timerTopPos+'px',
						'left':current_obj.timerLeftPos+'px'
					});
					audio7_html5_Audio_timer_b.css({
						'color':options.timerColor,
						'top':current_obj.timerTopPos+'px',
						'left':current_obj.timerLeftPos+audio7_html5_Audio_timer_a.width()+current_obj.seekBarLeftRightSpacing+current_obj.bufferWidth+current_obj.seekBarLeftRightSpacing+'px'
					});
					current_obj.bufferLeftPos=parseInt(current_obj.timerLeftPos+audio7_html5_Audio_timer_a.width()+11);
					audio7_html5_Audio_buffer.css({
						'left':current_obj.bufferLeftPos+'px'
					});
					audio7_html5_Audio_seek.css({
						'left':current_obj.bufferLeftPos+'px'
					});


					//the_bars
					current_obj.thebarsTopPos=current_obj.playTopPos;
					//current_obj.thebarsLeftPos=current_obj.titleLeftPos+current_obj.titleWidth+119;
					current_obj.thebarsLeftPos=current_obj.titleLeftPos+current_obj.titleWidth+25+91;  //91 some shifting position of the bars
					audio7_html5_the_bars.css({
						'top':current_obj.thebarsTopPos+'px',
						'left':current_obj.thebarsLeftPos+'px'
					});



					//volume
					current_obj.volumeTopPos=current_obj.timerTopPos;
					current_obj.volumeLeftPos=parseInt(audio7_html5_Audio_timer_b.css('left').substr(0,audio7_html5_Audio_timer_b.css('left').lastIndexOf('px')),10)+audio7_html5_Audio_timer_b.width()+20;
					audio7_html5_volumeMute_btn.css({
						'top':current_obj.volumeTopPos+'px',
						'left':current_obj.volumeLeftPos+'px'
					});
					current_obj.volumesliderTopPos=current_obj.volumeTopPos-audio7_html5_volumeSlider.height()-audio7_html5_volumeMute_btn.height();
					current_obj.volumesliderLeftPos=current_obj.volumeLeftPos+1;
					audio7_html5_volumeSlider.css({
						'top':current_obj.volumesliderTopPos+'px',
						'left':current_obj.volumesliderLeftPos+'px'
					});



					//shuffle
					current_obj.shuffleTopPos=current_obj.playTopPos+5;
					current_obj.shuffleLeftPos=current_obj.volumeLeftPos+2*current_obj.constantDistance+10;
					audio7_html5_shuffle_btn.css({
						'top':current_obj.shuffleTopPos+'px',
						'left':current_obj.shuffleLeftPos+'px'
					});
					if (options.shuffle) {
						audio7_html5_shuffle_btn.addClass('AudioShuffleON');
					}
					if (!options.showShuffleBut) {
						audio7_html5_shuffle_btn.css({
							'display':'none',
							'width':0,
							'height':0,
							'padding':0,
							'margin':0
						});
						current_obj.shuffleLeftPos=current_obj.volumeLeftPos+current_obj.constantDistance+10;
					}

					//download
					current_obj.downloadTopPos=current_obj.shuffleTopPos;
					current_obj.downloadLeftPos=current_obj.shuffleLeftPos+audio7_html5_shuffle_btn.width()+current_obj.constantDistance+3;
					audio7_html5_download_btn.css({
						'top':current_obj.downloadTopPos+'px',
						'left':current_obj.downloadLeftPos+'px'
					});
					if (!options.showDownloadBut) {
						audio7_html5_download_btn.css({
							'display':'none',
							'width':0,
							'height':0,
							'padding':0,
							'margin':0
						});
						current_obj.downloadLeftPos=current_obj.shuffleLeftPos+audio7_html5_shuffle_btn.width();
					}


					//show/hide playlist
					current_obj.showhideplaylistTopPos=current_obj.shuffleTopPos;
					current_obj.showhideplaylistLeftPos=current_obj.downloadLeftPos+audio7_html5_download_btn.width()+current_obj.constantDistance+3;
					audio7_html5_showHidePlaylist_btn.css({
						'top':current_obj.showhideplaylistTopPos+'px',
						'left':current_obj.showhideplaylistLeftPos+'px'
					});
					if (!options.showPlaylistBut) {
						audio7_html5_showHidePlaylist_btn.css({
							'display':'none',
							'width':0,
							'height':0,
							'padding':0,
							'margin':0
						});
						current_obj.showhideplaylistLeftPos=current_obj.downloadLeftPos+audio7_html5_download_btn.width();
					}


					//lyrics
					current_obj.lyricsTopPos=current_obj.shuffleTopPos;
					current_obj.lyricsLeftPos=current_obj.showhideplaylistLeftPos+audio7_html5_showHidePlaylist_btn.width()+current_obj.constantDistance+3;
					audio7_html5_lyrics_btn.css({
						'top':current_obj.lyricsTopPos+'px',
						'left':current_obj.lyricsLeftPos+'px'
					});
					if (!options.showLyricsBut) {
						audio7_html5_lyrics_btn.css({
							'display':'none',
							'width':0,
							'height':0,
							'padding':0,
							'margin':0
						});
						current_obj.lyricsLeftPos=current_obj.showhideplaylistLeftPos+audio7_html5_showHidePlaylist_btn.width();
					}

					//facebook
					current_obj.facebookTopPos=current_obj.shuffleTopPos;
					current_obj.facebookLeftPos=current_obj.lyricsLeftPos+audio7_html5_lyrics_btn.width()+current_obj.constantDistance;
					audio7_html5_facebook_btn.css({
						'top':current_obj.facebookTopPos+'px',
						'left':current_obj.facebookLeftPos+'px'
					});
					if (!options.showFacebookBut) {
						audio7_html5_facebook_btn.css({
							'display':'none',
							'width':0,
							'height':0,
							'padding':0,
							'margin':0
						});
						current_obj.facebookLeftPos=current_obj.lyricsLeftPos+audio7_html5_lyrics_btn.width();
					}



					//twitter
					current_obj.twitterTopPos=current_obj.shuffleTopPos;
					current_obj.twitterLeftPos=current_obj.facebookLeftPos+audio7_html5_facebook_btn.width()+current_obj.constantDistance+3;
					audio7_html5_twitter_btn.css({
						'top':current_obj.twitterTopPos+'px',
						'left':current_obj.twitterLeftPos+'px'
					});
					if (!options.showTwitterBut) {
						audio7_html5_twitter_btn.css({
							'display':'none',
							'width':0,
							'height':0,
							'padding':0,
							'margin':0
						});
						current_obj.twitterLeftPos=current_obj.facebookLeftPos+audio7_html5_facebook_btn.width();
					}


					//popup
					current_obj.popupTopPos=current_obj.shuffleTopPos;
					current_obj.popupLeftPos=current_obj.twitterLeftPos+audio7_html5_twitter_btn.width()+current_obj.constantDistance+3;
					audio7_html5_popup_btn.css({
						'top':current_obj.popupTopPos+'px',
						'left':current_obj.popupLeftPos+'px'
					});
					if (!options.showPopupBut) {
						audio7_html5_popup_btn.css({
							'display':'none',
							'width':0,
							'height':0,
							'padding':0,
							'margin':0
						});
						current_obj.popupLeftPos=current_obj.twitterLeftPos+audio7_html5_twitter_btn.width();
					}


					if (options.showClearFavoritesBut) {
							audio7_html5_clearFavoritesBut.css({
									'display':'block',
									'left':current_obj.shuffleLeftPos+'px'
							});
							/*audio7_html5_clearFavoritesBut.on( "click", function() {
									setCookie(options, 'cookie_favorites', '', options.favoritesCookieExpirationDays);
									current_obj.cookie_favorites_arr.splice(0,current_obj.cookie_favorites_arr.length);

									generatePlaylistByCateg(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_the_wrapper,audio7_html5_next_btn,audio7_html5_rewind_btn);
							});*/
					}



					//rewind
					current_obj.rewindTopPos=current_obj.constantDistance-2;
					current_obj.rewindLeftPos=current_obj.volumeLeftPos+2*current_obj.constantDistance+10-3;
					audio7_html5_rewind_btn.css({
						'bottom':current_obj.rewindTopPos+'px',
						'left':current_obj.rewindLeftPos+'px'
					});
					if (!options.showRewindBut) {
						audio7_html5_rewind_btn.css({
							'display':'none',
							'width':0,
							'height':0,
							'padding':0,
							'margin':0
						});
						current_obj.rewindLeftPos-=parseInt(current_obj.constantDistance/2,10);
					}



					//next, prev buttons
					current_obj.previousTopPos=current_obj.rewindTopPos;
					current_obj.previousLeftPos=current_obj.rewindLeftPos+audio7_html5_rewind_btn.width()+parseInt(current_obj.constantDistance/2,10);
					audio7_html5_prev_btn.css({
						'bottom':current_obj.previousTopPos+'px',
						'left':current_obj.previousLeftPos+'px'
					});

					current_obj.nextTopPos=current_obj.rewindTopPos;
					current_obj.nextLeftPos=current_obj.previousLeftPos+audio7_html5_prev_btn.width()+parseInt(current_obj.constantDistance/2,10);
					audio7_html5_next_btn.css({
						'bottom':current_obj.nextTopPos+'px',
						'left':current_obj.nextLeftPos+'px'
					});
					if (!options.showNextPrevBut) {
							audio7_html5_prev_btn.css({
								'display':'none'
							});
							audio7_html5_next_btn.css({
								'display':'none'
							});
					}



					//buy now
					current_obj.buyTopPos=current_obj.rewindTopPos;
					current_obj.buyLeftPos=current_obj.constantDistance;
					audio7_html5_buy_btn.css({
						'bottom':current_obj.buyTopPos+'px',
						'right':current_obj.buyLeftPos+'px'
					});
					if (!options.showBuyBut) {
						audio7_html5_buy_btn.css({
							'display':'none',
							'width':0,
							'height':0,
							'padding':0,
							'margin':0
						});
						//current_obj.buyLeftPos=current_obj.downloadLeftPos+audio7_html5_download_btn.width();
					}
			}



		function generateCategories(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_innerSelectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_the_wrapper,audio7_html5_next_btn,audio7_html5_rewind_btn) {
			  audio7_html5_thumbsHolder.stop(true,true);
			  current_obj.isCarouselScrolling=false;

			  audio7_html5_thumbsHolder.stop().animate({
				  'left': (-1)*audio7_html5_thumbsHolderVisibleWrapper.width()+'px'
			  }, 600, 'easeOutQuad', function() {
				  // Animation complete.
					audio7_html5_thumbsHolder.html("");

//current_obj.numberOfCategories=current_obj.category_arr.length;
					for (var j=0;j<current_obj.category_arr.length;j++) {
							current_obj.thumbsHolder_Thumb = $('<div class="thumbsHolder_ThumbOFF" rel="'+ j +'"><div class="padding">'+current_obj.category_arr[j]+'</div></div>');
							audio7_html5_thumbsHolder.append(current_obj.thumbsHolder_Thumb);


							current_obj.thumbsHolder_Thumb.css({
								/*"top":(current_obj.thumbsHolder_Thumb.height()+1)*j+'px',*/
								"background":options.categoryRecordBgOffColor,
								"border-bottom-color":options.categoryRecordBottomBorderOffColor,
								"color":options.categoryRecordTextOffColor
							});

							//activate current
							if (current_obj.category_arr[j]==current_obj.selectedCateg) {
								current_obj.current_img_no=j;
								current_obj.thumbsHolder_Thumb.css({
									"background":options.categoryRecordBgOnColor,
									"border-bottom-color":options.categoryRecordBottomBorderOnColor,
									"color":options.categoryRecordTextOnColor
								});
							}
					}

					current_obj.selectedCateg_total_images=current_obj.numberOfCategories;
					current_obj.categsAreListed=true;

					/*audio7_html5_thumbsHolderWrapper.height(2*options.playlistPadding+(current_obj.thumbsHolder_Thumb.height()+1)*((options.numberOfThumbsPerScreen<current_obj.numberOfCategories)?options.numberOfThumbsPerScreen:current_obj.numberOfCategories)+audio7_html5_selectedCategDiv.height()+audio7_html5_searchDiv.height()+2*options.selectedCategMarginBottom+4); //current_obj.thumbsHolder_Thumb.height()+1 - 1 is the border
					audio7_html5_thumbsHolderVisibleWrapper.height((current_obj.thumbsHolder_Thumb.height()+1)*((options.numberOfThumbsPerScreen<current_obj.numberOfCategories)?options.numberOfThumbsPerScreen:current_obj.numberOfCategories));	*/
					audio7_html5_thumbsHolderWrapper.height(2*options.playlistPadding+(current_obj.thumbsHolder_Thumb.height()+1)*options.numberOfThumbsPerScreen+audio7_html5_selectedCategDiv.height()+audio7_html5_searchDiv.height()+2*options.selectedCategMarginBottom); //current_obj.thumbsHolder_Thumb.height()+1 - 1 is the border
					audio7_html5_thumbsHolderVisibleWrapper.height((current_obj.thumbsHolder_Thumb.height()+1)*options.numberOfThumbsPerScreen);
					audio7_html5_playlistPadding.css({'padding':options.playlistPadding+'px'});

					current_obj.thumbsHolder_Thumbs=$('.thumbsHolder_ThumbOFF', audio7_html5_container);

					//the playlist scroller
					if (current_obj.numberOfCategories>options.numberOfThumbsPerScreen && options.showPlaylist) {
						if (options.isPlaylistSliderInitialized) {
							current_obj.audio7_html5_sliderVertical.slider( "destroy" );
						}
						current_obj.audio7_html5_sliderVertical.slider({
							orientation: "vertical",
							range: "min",
							min: 1,
							max: 100,
							step:1,
							value: 100,
							slide: function( event, ui ) {
								//alert( ui.value );
								carouselScroll(ui.value,current_obj,options,audio7_html5_thumbsHolder);
							}
						});
						 options.isPlaylistSliderInitialized=true;
						//var audio7_html5_selectedCategDiv = $('.selectedCategDiv', audio7_html5_container);
					    //var audio7_html5_searchDiv = $('.searchDiv', audio7_html5_container);
						current_obj.audio7_html5_sliderVertical.css({
							'display':'inline',
							'position':'absolute',
							'height':audio7_html5_thumbsHolderWrapper.height()-20-audio7_html5_selectedCategDiv.height()-2*options.selectedCategMarginBottom-audio7_html5_searchDiv.height()-2*options.playlistPadding+'px', // 24 is the height of  .slider-vertical.ui-slider .ui-slider-handle
							'left':audio7_html5_container.width()+2*options.playerPadding-current_obj.audio7_html5_sliderVertical.width()-options.playlistPadding+'px',
							'top':current_obj.audioPlayerHeight+options.playlistTopPos+options.playlistPadding+audio7_html5_selectedCategDiv.height()+options.selectedCategMarginBottom+2+'px'
						});

						if (!options.showPlaylistOnInit)
							current_obj.audio7_html5_sliderVertical.css({
								'opacity': 0,
								'display':'none'
							});
						options.showPlaylistOnInit=true; // to prevent sliderVertical disappereance after yo show the playlist

						$('.thumbsHolder_ThumbOFF', audio7_html5_container).css({
							'width':audio7_html5_container.width()+2*options.playerPadding-current_obj.audio7_html5_sliderVertical.width()-2*options.playlistPadding-3+'px'
						});

					} else {
						if (options.isPlaylistSliderInitialized) {
							current_obj.audio7_html5_sliderVertical.slider( "destroy" );
							options.isPlaylistSliderInitialized=false;
						}
						$('.thumbsHolder_ThumbOFF', audio7_html5_container).css({
							'width':audio7_html5_container.width()+2*options.playerPadding-2*options.playlistPadding+'px'
						});
					}





					//tumbs nav

					current_obj.thumbsHolder_Thumbs.on( "click", function() {
							var currentBut=$(this);
							var i=currentBut.attr('rel');
							current_obj.selectedCateg=current_obj.category_arr[i];
							setCookie(options,'cookie_firstCateg', current_obj.selectedCateg);
							audio7_html5_innerSelectedCategDiv.html(current_obj.selectedCateg);
							generatePlaylistByCateg(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_the_wrapper,audio7_html5_next_btn,audio7_html5_rewind_btn);

					});


					current_obj.thumbsHolder_Thumbs.on( "mouseover", function() {
						var currentBut=$(this);

						var my_bgColor="transparent";
						if (options.categoryRecordBgOnColor!='#') {
							my_bgColor=options.categoryRecordBgOnColor;
						}
						currentBut.css({
							"background":my_bgColor,
							"border-bottom-color":options.categoryRecordBottomBorderOnColor,
							"color":options.categoryRecordTextOnColor
						});
					});


					current_obj.thumbsHolder_Thumbs.on( "mouseout", function() {
						var currentBut=$(this);

						var my_bgColor="transparent";
						if (options.categoryRecordBgOffColor!='#') {
							my_bgColor=options.categoryRecordBgOffColor;
						}
						var i=currentBut.attr('rel');
						if (current_obj.current_img_no!=i){
							currentBut.css({
								"background":my_bgColor,
								"border-bottom-color":options.categoryRecordBottomBorderOffColor,
								"color":options.categoryRecordTextOffColor
							});
						}
					});

				//carouselScroll(-1,current_obj,options,audio7_html5_thumbsHolder);
				// mouse wheel
				audio7_html5_thumbsHolderVisibleWrapper.mousewheel(function(event, delta, deltaX, deltaY) {
					event.preventDefault();
					if (current_obj.selectedCateg_total_images>options.numberOfThumbsPerScreen) {
								var currentScrollVal=current_obj.audio7_html5_sliderVertical.slider( "value");
								//alert (currentScrollVal+' -- '+delta);
								if ( (parseInt(currentScrollVal)>1 && parseInt(delta)==-1) || (parseInt(currentScrollVal)<100 && parseInt(delta)==1) ) {
									currentScrollVal = currentScrollVal + delta;
									current_obj.audio7_html5_sliderVertical.slider( "value", currentScrollVal);
									carouselScroll(currentScrollVal,current_obj,options,audio7_html5_thumbsHolder)
									//alert (currentScrollVal);
								}
					}

				});

					audio7_html5_thumbsHolder.css({
						'top':0+'px'
					});
					audio7_html5_thumbsHolder.stop().animate({
						'left': 0+'px'
					}, 400, 'easeOutQuad', function() {
						// Animation complete.
			  		});
			  });




		}

		function generatePlaylistByCateg(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_the_wrapper,audio7_html5_next_btn,audio7_html5_rewind_btn) {
			audio7_html5_thumbsHolder.stop(true,true);
			current_obj.isCarouselScrolling=false;

			var titleLowerCases='';
			var elementFound=false;
			var animateDur=500;
			if (current_obj.is_very_first)
				animateDur=1;
			if (current_obj.search_val!='')
				animateDur=1;

			audio7_html5_thumbsHolder.stop().animate({
				  'left': (-1)*audio7_html5_thumbsHolderVisibleWrapper.width()+'px'
			}, animateDur, 'easeOutQuad', function() {
				  // Animation complete.
					var authorLowerCases;
				  audio7_html5_thumbsHolder.html("");

				  current_obj.selectedCateg_total_images=0;
				  for (var j=0;j<current_obj.playlist_arr.length;j++) {
					  elementFound=false;
					  //alert (current_obj.search_val);
					  if (current_obj.search_val!='') {
						  titleLowerCases=current_obj.playlist_arr[j]['title'].toLowerCase();
						  //alert (titleLowerCases.indexOf(current_obj.search_val));
						  if (titleLowerCases.indexOf(current_obj.search_val)!=-1) {
						  		elementFound=true;
						  }
						  if (options.searchAuthor) {
							  authorLowerCases=current_obj.playlist_arr[j]['author'].toLowerCase();
							  //alert (authorLowerCases.indexOf(current_obj.search_val));
							  if (authorLowerCases.indexOf(current_obj.search_val)!=-1) {
									elementFound=true;
							  }
						  }
					  } else {
						  if (current_obj.playlist_arr[j]['category'].indexOf(current_obj.selectedCateg+';')!=-1) {
							  elementFound=true;
						  }
					  }

					  if (elementFound) {
						  current_obj.selectedCateg_total_images++;
						  current_obj.thumbsHolder_Thumb = $('<div class="thumbsHolder_ThumbOFF" rel="'+ (current_obj.selectedCateg_total_images-1) +'" data-origID="'+ j +'"><div class="padding">'+((options.showPlaylistNumber)?(current_obj.selectedCateg_total_images)+'. ':'')+current_obj.playlist_arr[j]['title']+'</div><div class="playlistFavoriteBut_OFF'+((current_obj.cookie_favorites_arr.indexOf(j.toString())>-1)?" playlistFavoriteBut_ON":"")+'"></div></div>');
						  audio7_html5_thumbsHolder.append(current_obj.thumbsHolder_Thumb);
						  if (current_obj.thumbsHolder_ThumbHeight==0) {
						  		current_obj.thumbsHolder_ThumbHeight=current_obj.thumbsHolder_Thumb.height();
						  }
							if (options.showClearFavoritesBut) {
									$('.playlistFavoriteBut_OFF',current_obj.thumbsHolder_Thumb).css({
											'display':'block'
									});
									$('.playlistFavoriteBut_OFF',current_obj.thumbsHolder_Thumb).on( "click", function(event) {
											event.stopPropagation();
											//alert ($(this).attr('class'));
											if ($(this).attr('class')=='playlistFavoriteBut_OFF') { // activate
													$(this).addClass('playlistFavoriteBut_ON');
													current_obj.cookie_favorites_arr.push(parseInt($(this).parent().attr('data-origID'),10));
													current_obj.cookie_favorites_arr.sort(function(a, b){return a - b});
													//console.log('new: '+current_obj.cookie_favorites_arr+' --- '+current_obj.instanceID);
													setCookieFav(options, 'cookie_favorites'+current_obj.instanceID, current_obj.cookie_favorites_arr.join('|'), options.favoritesCookieExpirationDays);
											} else { //deactivate
													$(this).removeClass('playlistFavoriteBut_ON');
													removeFromCookieArray(current_obj,parseInt($(this).parent().attr('data-origID'),10));
													setCookieFav(options, 'cookie_favorites'+current_obj.instanceID, current_obj.cookie_favorites_arr.join('|'), options.favoritesCookieExpirationDays);
											}
											//alert ('add to favorites: '+$(this).parent().attr('data-origID'));

									});
							} else {
                                $('.playlistFavoriteBut_OFF',current_obj.thumbsHolder_Thumb).css({
                                    'display':'none'
                            	});
                            }

						  var my_bgColor="transparent";
						  if (options.playlistRecordBgOffColor!='#') {
							  my_bgColor=options.playlistRecordBgOffColor;
						  }
						  current_obj.thumbsHolder_Thumb.css({
							  /*"top":(current_obj.thumbsHolder_ThumbHeight+1)*current_obj.selectedCateg_total_images+'px',*/
							  "background":my_bgColor,
							  "border-bottom-color":options.playlistRecordBottomBorderOffColor,
							  "color":options.playlistRecordTextOffColor
						  });



						  current_obj.current_img_no=0;

						  //activate playing one
						  if (current_obj.origID==$("div[rel=\'"+(current_obj.selectedCateg_total_images-1)+"\']").attr('data-origID')){
							  current_obj.thumbsHolder_Thumb.css({
								  "background":options.playlistRecordBgOnColor,
								  "border-bottom-color":options.playlistRecordBottomBorderOnColor,
								  "color":options.playlistRecordTextOnColor
							  });
						  }
					  }
				  }




				  current_obj.categsAreListed=false;


				  /*audio7_html5_thumbsHolderWrapper.height(2*options.playlistPadding+(current_obj.thumbsHolder_ThumbHeight+1)*((options.numberOfThumbsPerScreen<current_obj.selectedCateg_total_images)?options.numberOfThumbsPerScreen:current_obj.selectedCateg_total_images)+audio7_html5_selectedCategDiv.height()+audio7_html5_searchDiv.height()+2*options.selectedCategMarginBottom+4); //current_obj.thumbsHolder_ThumbHeight+1 - 1 is the border
				  audio7_html5_thumbsHolderVisibleWrapper.height((current_obj.thumbsHolder_ThumbHeight+1)*((options.numberOfThumbsPerScreen<current_obj.selectedCateg_total_images)?options.numberOfThumbsPerScreen:current_obj.selectedCateg_total_images));	*/
				  audio7_html5_thumbsHolderWrapper.height(2*options.playlistPadding+(current_obj.thumbsHolder_ThumbHeight+1)*options.numberOfThumbsPerScreen+audio7_html5_selectedCategDiv.height()+audio7_html5_searchDiv.height()+2*options.selectedCategMarginBottom); //current_obj.thumbsHolder_ThumbHeight+1 - 1 is the border
				  audio7_html5_thumbsHolderVisibleWrapper.height((current_obj.thumbsHolder_ThumbHeight+1)*options.numberOfThumbsPerScreen);
				  audio7_html5_playlistPadding.css({'padding':options.playlistPadding+'px'});

				  current_obj.thumbsHolder_Thumbs=$('.thumbsHolder_ThumbOFF', audio7_html5_container);

				  //alert (audio7_html5_thumbsHolderWrapper.height());

				  var wrapperHeight=current_obj.audioPlayerHeight+audio7_html5_thumbsHolderWrapper.height()+options.playlistTopPos;
				  if (!options.showPlaylist || !options.showPlaylistOnInit) {
					  wrapperHeight=current_obj.audioPlayerHeight;
				  }
				  audio7_html5_the_wrapper.css({
						/*'border':'1px solid #FF0000',*/
						'width':audio7_html5_container.width()+2*options.playerPadding+'px',
						'height':wrapperHeight+'px'
			      });

				  //the playlist scroller
				  if (current_obj.selectedCateg_total_images>options.numberOfThumbsPerScreen && options.showPlaylist) {

					  if (options.isPlaylistSliderInitialized) {
						  current_obj.audio7_html5_sliderVertical.slider( "destroy" );
					  }
					  current_obj.audio7_html5_sliderVertical.slider({
						  orientation: "vertical",
						  range: "min",
						  min: 1,
						  max: 100,
						  step:1,
						  value: 100,
						  slide: function( event, ui ) {
							  //alert( ui.value );
							  carouselScroll(ui.value,current_obj,options,audio7_html5_thumbsHolder);
						  }
					  });
					  options.isPlaylistSliderInitialized=true;
				  //var audio7_html5_selectedCategDiv = $('.selectedCategDiv', audio7_html5_container);
				  //var audio7_html5_searchDiv = $('.searchDiv', audio7_html5_container);
					  current_obj.audio7_html5_sliderVertical.css({
						  'display':'inline',
						  'position':'absolute',
						  'height':audio7_html5_thumbsHolderWrapper.height()-20-audio7_html5_selectedCategDiv.height()-2*options.selectedCategMarginBottom-audio7_html5_searchDiv.height()-2*options.playlistPadding+'px', // 24 is the height of  .slider-vertical.ui-slider .ui-slider-handle
						  'left':audio7_html5_container.width()+2*options.playerPadding-current_obj.audio7_html5_sliderVertical.width()-options.playlistPadding+'px',
						  'top':current_obj.audioPlayerHeight+options.playlistTopPos+options.playlistPadding+audio7_html5_selectedCategDiv.height()+options.selectedCategMarginBottom+2+'px'
					  });

					  if (!options.showPlaylistOnInit)
						  current_obj.audio7_html5_sliderVertical.css({
							  'opacity': 0,
							  'display':'none'
						  });
					  options.showPlaylistOnInit=true; // to prevent sliderVertical disappereance after you show the playlist

					  $('.thumbsHolder_ThumbOFF', audio7_html5_container).css({
						  'width':audio7_html5_container.width()+2*options.playerPadding-current_obj.audio7_html5_sliderVertical.width()-2*options.playlistPadding-3+'px'
					  });

				  } else {
					  if (options.isPlaylistSliderInitialized) {
							current_obj.audio7_html5_sliderVertical.slider( "destroy" );
							options.isPlaylistSliderInitialized=false;
					  }
					  $('.thumbsHolder_ThumbOFF', audio7_html5_container).css({
						  'width':audio7_html5_container.width()+2*options.playerPadding-2*options.playlistPadding+'px'
					  });
				  }

				//tumbs nav
				current_obj.thumbsHolder_Thumbs.on( "click", function() {
					if (!current_obj.is_changeSrc) {
						options.autoPlay=true;
						var currentBut=$(this);
						var i=currentBut.attr('rel');

						var my_bgColor="transparent";
						if (options.playlistRecordBgOffColor!='#') {
							my_bgColor=options.playlistRecordBgOffColor;
						}
						current_obj.thumbsHolder_Thumbs.css({
							"background":my_bgColor,
							"border-bottom-color":options.playlistRecordBottomBorderOffColor,
							"color":options.playlistRecordTextOffColor
						});

						current_obj.current_img_no=i;
						current_obj.origID=$("div[rel=\'"+current_obj.current_img_no+"\']").attr('data-origID');
						if (options.continuouslyPlayOnAllPages) {
								setCookie(options,'cookie_current_img_no', current_obj.current_img_no);
								setCookie(options,'cookie_origID', current_obj.origID);
						}
						changeSrc(current_obj,options,audio7_html5_thumbsHolder,audio7_html5_container,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_rewind_btn);
					}
				});


				current_obj.thumbsHolder_Thumbs.on( "mouseover", function() {
					var currentBut=$(this);

					var my_bgColor="transparent";
					if (options.playlistRecordBgOnColor!='#') {
						my_bgColor=options.playlistRecordBgOnColor;
					}

					currentBut.css({
						"background":my_bgColor,
						"border-bottom-color":options.playlistRecordBottomBorderOnColor,
						"color":options.playlistRecordTextOnColor
					});
				});


				current_obj.thumbsHolder_Thumbs.on( "mouseout", function() {
					var currentBut=$(this);

					var my_bgColor="transparent";
					if (options.playlistRecordBgOffColor!='#') {
						my_bgColor=options.playlistRecordBgOffColor;
					}

					var i=currentBut.attr('rel');
					if (current_obj.origID!=$("div[rel=\'"+i+"\']").attr('data-origID')){
						currentBut.css({
							"background":my_bgColor,
							"border-bottom-color":options.playlistRecordBottomBorderOffColor,
							"color":options.playlistRecordTextOffColor
						});
					}
				});


				// mouse wheel
				audio7_html5_thumbsHolderVisibleWrapper.mousewheel(function(event, delta, deltaX, deltaY) {
					event.preventDefault();
					if (current_obj.selectedCateg_total_images>options.numberOfThumbsPerScreen) {
								var currentScrollVal=current_obj.audio7_html5_sliderVertical.slider( "value");
								//alert (currentScrollVal+' -- '+delta);
								if ( (parseInt(currentScrollVal)>1 && parseInt(delta)==-1) || (parseInt(currentScrollVal)<100 && parseInt(delta)==1) ) {
									currentScrollVal = currentScrollVal + delta;
									current_obj.audio7_html5_sliderVertical.slider( "value", currentScrollVal);
									carouselScroll(currentScrollVal,current_obj,options,audio7_html5_thumbsHolder)
									//alert (currentScrollVal);
								}
					}
				});

				//console.log("changedCategFromApi: "+current_obj.changedCategFromApi);
				if (current_obj.changedCategFromApi) {
					setTimeout(function(){
								//console.log($("[rel=0]", audio7_html5_container).attr('data-origid'));
								$("[rel=0]", audio7_html5_container).click();
								//remove class for external buttons
								$('[id^=changePlaylist_]').removeClass('external_pause_button');
								//change_class_for_external_button
								$('#changePlaylist_'+current_obj.selectedCateg).addClass('external_pause_button');
					}, 200);
				}
				current_obj.changedCategFromApi=false;

				audio7_html5_thumbsHolder.css({
					'top':0+'px'
				});
				audio7_html5_thumbsHolder.stop().animate({
					'left': 0+'px'
				}, 400, 'easeOutQuad', function() {
					// Animation complete.
				});


				if (options.shuffle && current_obj.cookie_current_img_no==undefined ) {
					  current_obj.is_very_first=false;
						audio7_html5_next_btn.click();
				}

			});


		}


		function manageCss3Animations(options,current_obj,audio7_html5_container,is_playing) {
			if (!current_obj.isMinified) {
					if (is_playing==false) {
							$('.sound', audio7_html5_container).css({
								'-webkit-animation-play-state':'paused',
								'-moz-animation-play-state':'paused',
								'-ms-animation-play-state':'paused',
								'-o-animation-play-state':'paused',
								'animation-play-state':'paused'
							});
							$('.sound2', audio7_html5_container).css({
								'-webkit-animation-play-state':'paused',
								'-moz-animation-play-state':'paused',
								'-ms-animation-play-state':'paused',
								'-o-animation-play-state':'paused',
								'animation-play-state':'paused'
							});
							if (options.playerWidth<645) {
									if (options.showVinylRecord) {
											$('.pickUp_on', audio7_html5_container).css({
												'visibility':'hidden',
												'display':'none'
											});
											$('.pickUp_off', audio7_html5_container).css({
												'visibility':'hidden',
												'display':'none'
											});
									}
							} else {
									if (options.showVinylRecord) {
											$('.pickUp_on', audio7_html5_container).css({
												'visibility':'hidden',
												'display':'none'
											});
											$('.pickUp_off', audio7_html5_container).css({
												'visibility':'visible',
												'display':'block'
											});
									}
							}
					} else {
							$('.sound', audio7_html5_container).css({
								'-webkit-animation-play-state':'running',
								'-moz-animation-play-state':'running',
								'-ms-animation-play-state':'running',
								'-o-animation-play-state':'running',
								'animation-play-state':'running'
							});
							$('.sound2', audio7_html5_container).css({
								'-webkit-animation-play-state':'running',
								'-moz-animation-play-state':'running',
								'-ms-animation-play-state':'running',
								'-o-animation-play-state':'running',
								'animation-play-state':'running'
							});

							if (options.playerWidth<645) {
									if (options.showVinylRecord) {
											$('.pickUp_on', audio7_html5_container).css({
												'visibility':'hidden',
												'display':'none'
											});
											$('.pickUp_off', audio7_html5_container).css({
												'visibility':'hidden',
												'display':'none'
											});
									}
							} else {
									if (options.showVinylRecord) {
											$('.pickUp_on', audio7_html5_container).css({
												'visibility':'visible',
												'display':'block'
											});
											$('.pickUp_off', audio7_html5_container).css({
												'visibility':'hidden',
												'display':'none'
											});
									}
							}
					}
			}
		}



		function popUpCleaner(audio7_html5_the_wrapper) {
				var allElems=$('body *');
				//var allElems=$('body > *'); //first level
				//alert (audio7_html5_the_wrapper.attr('class'));
				var lbg_target;
				var my_class_name;
				allElems.each(function(){
					lbg_target=$(this);
					//alert (this.className+'  --  '+this.innerHTML+'  --  '+ $.contains(lbg_target[0],audio7_html5_the_wrapper[0]));
					//alert (this.className+'  --  '+ $.contains(lbg_target[0],audio7_html5_the_wrapper[0])+'  --  '+$.contains(audio7_html5_the_wrapper[0], lbg_target[0]));
					//alert (lbg_target.attr('class')+'  --  '+$.contains(lbg_target[0],audio7_html5_the_wrapper[0]) );
					if ( $.contains(lbg_target[0],audio7_html5_the_wrapper[0]) ) {
						//is a div which contains the player
						//alert ("remove website  parent of the player: "+this.className);
						audio7_html5_the_wrapper.unwrap();
						popUpCleaner(audio7_html5_the_wrapper);
					} else {
						//alert (this.className+' --  '+$(this).find('.the_wrapper').className);
						//alert ( $.contains(audio7_html5_the_wrapper[0], lbg_target[0])+'  ||  '+this.className );
						//here 1.2.3 start
						my_class_name=this.className;
						my_class_name=String(my_class_name);
						//here 1.2.3 end
						if ( $.contains(audio7_html5_the_wrapper[0], lbg_target[0]) || my_class_name=='the_wrapper' || my_class_name.indexOf("audio7_html5")!=-1 ) {
							//nothing, is the player or inside the player
							//alert ("nothing: "+this.className);
						} else {
							//this.innerHTML="";
							//alert ("remove: "+this.className);
							this.remove();
						}
					}
				});
				//$('div').not('.the_wrapper').remove();
	  }



		function setCookie(options,c_name,value,maxAgeSeconds)
		{
				/*var exdate=new Date();
				//alert ("now: "+exdate.toUTCString());
				exdate.setDate(exdate.getDate() + exdays);
				var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString())+";path=/";
				//alert (c_value);
				document.cookie=c_name + "=" + c_value;*/
				//here 1.2.3 start
				if (maxAgeSeconds==null) {
					maxAgeSeconds=24*60*60;
				}
				//alert (c_name+'  ---  '+maxAgeSeconds);
				var maxAge = "; max-age=" + maxAgeSeconds;
				var cookieLevel='; path=/';
				document.cookie = encodeURI(c_name) + "=" + encodeURI(value) + maxAge+cookieLevel;
				//here 1.2.3 end
		}

	  function getCookie(options,c_name)
	  {
		  if (options.continuouslyPlayOnAllPages) {
			  var i,x,y,ARRcookies=document.cookie.split(";");
			  for (i=0;i<ARRcookies.length;i++)
			  {
				x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
				y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
				x=x.replace(/^\s+|\s+$/g,"");
				if (x==c_name)
				  {
				  return unescape(y);
				  }
				}
		  }
	  }


		function findNextVideoNumbers(current_obj,options,navigationFlag) {
					if (options.showClearFavoritesBut & current_obj.cookie_favorites_arr.length>0 && !current_obj.thumbsClicked) { // i have favorites
									current_obj.cur_favorite++;
									if (current_obj.cookie_favorites_arr.length>1 && current_obj.current_img_no===parseInt( $("div[data-origID=\'"+current_obj.cookie_favorites_arr[current_obj.cur_favorite]+"\']").attr('rel'),10)) {
										current_obj.cur_favorite++;
									}
									if (current_obj.cur_favorite>=current_obj.cookie_favorites_arr.length) {
										current_obj.cur_favorite=0;
									}
									current_obj.current_img_no=$("div[data-origID=\'"+current_obj.cookie_favorites_arr[current_obj.cur_favorite]+"\']").attr('rel');
									current_obj.origID=parseInt(current_obj.cookie_favorites_arr[current_obj.cur_favorite],10);
									current_obj.previousOrigID=current_obj.origID;
									//console.log("cookie_favorites_arr: "+current_obj.cookie_favorites_arr);
									//console.log(current_obj.cur_favorite+'   -------    '+current_obj.current_img_no+'   ----   '+current_obj.origID);
					} else { //no favorites
										if (options.shuffle) {
											var new_current_img_no=Math.ceil(Math.random() * (current_obj.selectedCateg_total_images-1));
											if (new_current_img_no!=current_obj.current_img_no) {
												current_obj.current_img_no=new_current_img_no;
											} else {
												current_obj.current_img_no=Math.ceil(Math.random() * (current_obj.selectedCateg_total_images-1));
											}
										} else {
											if (navigationFlag=='next') {
												if (current_obj.current_img_no==current_obj.selectedCateg_total_images-1)
													current_obj.current_img_no=0;
												else
													current_obj.current_img_no++;
											} else {
												if (current_obj.current_img_no-1<0)
													current_obj.current_img_no=current_obj.selectedCateg_total_images-1;
												else
													current_obj.current_img_no--;
											}
										}

										current_obj.origID=$("div[rel=\'"+current_obj.current_img_no+"\']").attr('data-origID');
				}
		};


		function getInternetExplorerVersion()
		// -1 - not IE
		// 7,8,9 etc
		{
		   var rv = -1; // Return value assumes failure.
		   if (navigator.appName == 'Microsoft Internet Explorer')
		   {
			  var ua = navigator.userAgent;
			  var re  = new RegExp("MSIE ([0-9]{1,}[\.0-9]{0,})");
			  if (re.exec(ua) != null)
				 rv = parseFloat( RegExp.$1 );
		   }
		   return parseInt(rv,10);
		}

		function cancelAll(options,audio7_html5_container) {
			//alert ($("audio").attr('id'));
			//$("audio")[0].pause();
			$("audio").each(function() {
				$('.AudioPlay').removeClass('AudioPause');
				$(this)[0].pause();
			});
			//cancel animations
			$('.sound').css({
				'-webkit-animation-play-state':'paused',
				'-moz-animation-play-state':'paused',
				'-ms-animation-play-state':'paused',
				'-o-animation-play-state':'paused',
				'animation-play-state':'paused'
			});
			$('.sound2').css({
				'-webkit-animation-play-state':'paused',
				'-moz-animation-play-state':'paused',
				'-ms-animation-play-state':'paused',
				'-o-animation-play-state':'paused',
				'animation-play-state':'paused'
			});

			if (options.playerWidth>=645) {
				$('.pickUp_on').each(function() {
					if ( $(this).css('display')=='block' ) {
							$(this).css({
								'visibility':'hidden',
								'display':'none'
							});
						 $('.pickUp_off',$(this).parent()).css({
								'visibility':'visible',
								'display':'block'
							});
					}
				});
					/*if (options.showVinylRecord) {
							$('.pickUp_on').css({
								'visibility':'hidden',
								'display':'none'
							});
							$('.pickUp_off').css({
								'visibility':'visible',
								'display':'block'
							});
					}*/
			}
		}

		function setCookieFav(options,c_name,value,exdays)
		{
			if (options.showClearFavoritesBut) {
				var exdate=new Date();
				exdate.setDate(exdate.getDate() + exdays);
				var c_value=escape(value) + ((exdays==null) ? "" : "; expires="+exdate.toUTCString())+";path=/";
				document.cookie=c_name + "=" + c_value;
			}
		}

		function getCookieFav(options,c_name)
		{
			if (options.showClearFavoritesBut) {
				var i,x,y,ARRcookies=document.cookie.split(";");
				for (i=0;i<ARRcookies.length;i++)
				{
					x=ARRcookies[i].substr(0,ARRcookies[i].indexOf("="));
					y=ARRcookies[i].substr(ARRcookies[i].indexOf("=")+1);
					x=x.replace(/^\s+|\s+$/g,"");
					if (x==c_name)
					{
					return unescape(y);
					}
					}
			}
		}

		function removeFromCookieArray(current_obj,what_to_remove) {
				var elem_index = current_obj.cookie_favorites_arr.indexOf(what_to_remove);
				if (elem_index <= -1) {
					var elem_index = current_obj.cookie_favorites_arr.indexOf(what_to_remove.toString());
				}
				if (elem_index > -1) {
				  current_obj.cookie_favorites_arr.splice(elem_index, 1);
				}
				//console.log(current_obj.cookie_favorites_arr);
		}


	$.audio7_html5 = {version: '1.0'};

	//core
	$.fn.audio7_html5 = function(options) {

		var options = $.extend({},$.fn.audio7_html5.defaults, options);
		//parse it
		return this.each(function() {
			var audio7_html5_Audio = $(this);


			//the controllers
			var audio7_html5_controlsDef = $('<div class="AudioControls"> <a class="AudioCloseBut" title="Minimize"></a> <a class="AudioRewind" title="Rewind"></a><a class="AudioShuffle" title="Shuffle Playlist"></a><a class="AudioDownload" title="Download File"></a><a class="AudioBuy" title="Buy Now"></a><a class="AudioLyrics" title="Lyrics"></a><a class="AudioFacebook" title="Facebook"></a><a class="AudioTwitter" title="Twitter"></a><a class="AudioPopup" title="Popup"></a><div class="clearFavoritesBut" title="Clear Favorites"></div><a class="AudioPlay" title="Play/Pause"></a><a class="AudioPrev" title="Previous"></a><a class="AudioNext" title="Next"></a><a class="AudioShowHidePlaylist" title="Show/Hide Playlist"></a><a class="VolumeButton" title="Mute/Unmute"></a><div class="VolumeSlider"></div> <div class="AudioTimer_a">00:00</div><div class="AudioTimer_b">00:00</div>  </div>   <div class="AudioBuffer"></div><div class="AudioSeek"></div><div class="songTitle"><div class="songTitleInside"></div></div>  <div class="songAuthor"></div>    <div class="thumbsHolderWrapper"><div class="playlistPadding"><div class="selectedCategDiv"><div class="innerSelectedCategDiv">CATEGORIES</div></div> <div class="thumbsHolderVisibleWrapper"><div class="thumbsHolder"></div></div><div class="searchDiv"><input class="search_term" type="text" value="search..." /></div></div></div>  <div class="slider-vertical"></div> <div class="ximage_lbg"></div>  ');
			var audio7_html5_the_bars=$('<div class="barsContainer"><div id="bars" class="perspectiveDownZero"><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2" style="display:none;"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div><div class="bar sound2"></div></div><div id="bars"><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound" style="display:none;"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div><div class="bar sound"></div></div></div>');
			var audio7_html5_vinyl_record_on=$('<div class="pickUp_on"><div class="disc xWheel1"></div><div class="openUpLeftRetournApollo ac"></div></div>');
			var audio7_html5_vinyl_record_off=$('<div class="pickUp_off"><div class="disc pause_lbg"></div><div class="openUpLeftRetournApollo2 ac"></div></div>');


			//the elements
			var audio7_html5_container = audio7_html5_Audio.parent('.audio7_html5');
			//var audio7_html5_border = $(this).parent();
			//alert (audio7_html5_border.attr('class')+'   ---   '+audio7_html5_container.attr('class'));  // the same

			audio7_html5_container.addClass(options.skin);
			audio7_html5_container.append(audio7_html5_controlsDef);
			audio7_html5_container.append(audio7_html5_the_bars);
			audio7_html5_container.append(audio7_html5_vinyl_record_on);
			audio7_html5_container.append(audio7_html5_vinyl_record_off);


			var audio7_html5_controls = $('.AudioControls', audio7_html5_container);
			var audio7_html5_rewind_btn = $('.AudioRewind', audio7_html5_container);
			var audio7_html5_shuffle_btn = $('.AudioShuffle', audio7_html5_container);
			var audio7_html5_download_btn = $('.AudioDownload', audio7_html5_container);
			var audio7_html5_buy_btn = $('.AudioBuy', audio7_html5_container);
			var audio7_html5_lyrics_btn = $('.AudioLyrics', audio7_html5_container);
			var audio7_html5_facebook_btn = $('.AudioFacebook', audio7_html5_container);
			var audio7_html5_twitter_btn = $('.AudioTwitter', audio7_html5_container);
			var audio7_html5_popup_btn = $('.AudioPopup', audio7_html5_container);
			var audio7_html5_clearFavoritesBut = $('.clearFavoritesBut', audio7_html5_container);


			var audio7_html5_play_btn = $('.AudioPlay', audio7_html5_container);
			var audio7_html5_prev_btn = $('.AudioPrev', audio7_html5_container);
			var audio7_html5_next_btn = $('.AudioNext', audio7_html5_container);
			var audio7_html5_showHidePlaylist_btn = $('.AudioShowHidePlaylist', audio7_html5_container);
			var audio7_html5_volumeMute_btn = $('.VolumeButton', audio7_html5_container);
			var audio7_html5_volumeSlider = $('.VolumeSlider', audio7_html5_container);
			var audio7_html5_minimize_btn = $('.AudioCloseBut', audio7_html5_container);
			var audio7_html5_Audio_timer_a = $('.AudioTimer_a', audio7_html5_container);
			var audio7_html5_Audio_timer_b = $('.AudioTimer_b', audio7_html5_container);
			var audio7_html5_Title = $('.songTitle', audio7_html5_container);
			var audio7_html5_TitleInside = $('.songTitleInside', audio7_html5_container);
			var audio7_html5_Author = $('.songAuthor', audio7_html5_container);
			var audio7_html5_ximage = $('.ximage_lbg', audio7_html5_container);


			var audio7_html5_Audio_buffer = $('.AudioBuffer', audio7_html5_container);
			var audio7_html5_Audio_seek = $('.AudioSeek', audio7_html5_container);

			audio7_html5_container.wrap("<div class='the_wrapper'></div>");
			var audio7_html5_the_wrapper = audio7_html5_container.parent();
			audio7_html5_the_wrapper.css ({
				'margin':'0 auto'
			});

			var ver_ie=getInternetExplorerVersion();


			audio7_html5_buy_btn.attr("title",options.buyButTitle);
			audio7_html5_lyrics_btn.attr("title",options.lyricsButTitle);






			/****if (val.indexOf("ipad") != -1 || val.indexOf("iphone") != -1 || val.indexOf("ipod") != -1 || val.indexOf("webos") != -1) {
				//audio7_html5_controls.css({margin-top:-20px;});
				audio7_html5_container.css({
					'padding-top':'0px'
				});
			}****/

			var randNo=Math.floor(Math.random()*100000);
			var current_obj = {
				current_img_no:0,
				origID:0,
				is_very_first:true,
				total_images:0,
				selectedCateg_total_images:0,
				numberOfCategories:0,
				is_seeking:false,
				is_changeSrc:false,
				is_buffer_complete:false,
				timeupdateInterval:'',
				totalTime:'',
				playlist_arr:'',
				isCarouselScrolling:false,
				isAuthorTitleInsideScrolling:false,
				curSongText:'',
				authorTitleInsideWait:0,
				audioPlayerWidth:0,
				audioPlayerHeight:0,
				seekBarLeftRightSpacing:11,

				category_arr:'',
				selectedCateg:'',
				categsAreListed:false,
				thumbsHolder_Thumb:$('<div class="thumbsHolder_ThumbOFF" rel="0"><div class="padding">test</div></div>'),
				thumbsHolder_ThumbHeight:0,
				thumbsHolder_Thumbs:'',

				search_val:'',

				constantDistance:15,
				timerTopPos:0,
				timerLeftPos:0,
				bufferTopPos:0,
				bufferLeftPos:0,
				bufferWidth:461,
				thebarsTopPos:0,
				thebarsLeftPos:0,
				titleWidth:0,
				authorTopPos:0,
				authorLeftPos:0,
				titleTopPos:0,
				titleLeftPos:0,
				imageTopPos:0,
				imageLeftPos:0,
				playTopPos:0,
				playLeftPos:0,
				previousTopPos:0,
				previousLeftPos:0,
				nextTopPos:0,
				nextLeftPos:0,
				volumeTopPos:0,
				volumeLeftPos:0,
				volumesliderTopPos:0,
				volumesliderLeftPos:0,
				showhideplaylistTopPos:0,
				showhideplaylistLeftPos:0,
				rewindTopPos:0,
				rewindLeftPos:0,
				shuffleTopPos:0,
				shuffleLeftPos:0,
				downloadTopPos:0,
				downloadLeftPos:0,
				buyTopPos:0,
				buyLeftPos:0,
				lyricsTopPos:0,
				lyricsLeftPos:0,
				facebookTopPos:0,
				facebookLeftPos:0,
				twitterTopPos:0,
				twitterLeftPos:0,
				popupTopPos:0,
				popupLeftPos:0,
				minimizeTopPos:0,
				minimizeLeftPos:0,
				isMinified:false,
				isPlaylistVisible:true,
				cookie_timePlayed:0,
				cookie_current_img_no:0,
				cookie_origID:0,
				cookie_initialVolume:0,
				cookie_muteVolume:0,
				cookie_autoPlay:false,
				cookie_shuffle:false,
				cookie_firstCateg:'',
				cookie_isMinified:false,
				cookie_popupWin:'',
				cookie_favorites_arr:'',
				the_cookie_favorites:'',
				cur_favorite:-1,


				origParentFloat:'',
				origParentPaddingTop:'',
				origParentPaddingRight:'',
				origParentPaddingBottom:'',
				origParentPaddingLeft:'',

				windowWidth:0,

				audioObj:'',

				stickyFixedPlayerWidth:980,
				changedCategFromApi:false,
				audioID:'audio7_audio_tag_id_'+randNo,
				instanceID:$(this).attr('id')

			};
			//current_obj.audioID=audio7_html5_Audio.attr('id');
			current_obj.html5_audio_tag=$('<audio id="'+current_obj.audioID+'" preload="metadata"></audio>');
			audio7_html5_container.append(current_obj.html5_audio_tag);


			current_obj.cookie_popupWin=getCookie(options,'cookie_popupWin');


			current_obj.cookie_favorites_arr=new Array();
			if (options.showClearFavoritesBut) {
						current_obj.the_cookie_favorites=getCookieFav(options,'cookie_favorites'+current_obj.instanceID);
						if (current_obj.the_cookie_favorites!='' && current_obj.the_cookie_favorites!=undefined) {
								current_obj.cookie_favorites_arr=current_obj.the_cookie_favorites.split('|');
								for (var kk = 0; kk < current_obj.cookie_favorites_arr.lenght; kk++) {
								    current_obj.cookie_favorites_arr[kk] = parseInt(current_obj.cookie_favorites_arr[kk], 10);
								}
								current_obj.cookie_favorites_arr.sort(function(a, b){return a - b});
						}
			}


			//alert (window.self.name);
			if (window.self.name=='audio7_PopupName') {
				options.sticky=false;
				options.showPopupBut=false;
				audio7_html5_the_wrapper.css({
					'top':(-1)*parseInt(audio7_html5_ximage.css('top').substring(0, audio7_html5_ximage.css('top').length-2),10)+'px',
					'left':0,
					'position':'absolute'
				});
				audio7_html5_the_wrapper.unwrap();
				$('body').css({
					'background-color':'#999999 !important',
					'min-width':'305px'
				});
				document.getElementsByTagName("body")[0].style.marginTop='0px';
				document.getElementsByTagName("body")[0].style.marginBottom='0px';
				document.getElementsByTagName("body")[0].style.marginLeft='0px';
				document.getElementsByTagName("body")[0].style.marginRight='0px';
				document.getElementsByTagName("body")[0].style.paddingTop='0px';
				document.getElementsByTagName("body")[0].style.paddingBottom='0px';
				document.getElementsByTagName("body")[0].style.paddingLeft='0px';
				document.getElementsByTagName("body")[0].style.paddingRight='0px';

				/*for (i=0;i<20;i++) {
					//alert (audio7_html5_the_wrapper.parent().className);
					audio7_html5_the_wrapper.unwrap();
				}*/
				popUpCleaner(audio7_html5_the_wrapper);

			}





			//options.playerWidth=audio7_html5_container.parent().parent().width();
			options.playerWidth=audio7_html5_container.parent().width();
			if (options.sticky) {
				//options.playerWidth=$(window).width();
				options.playerWidth=current_obj.stickyFixedPlayerWidth+2*current_obj.constantDistance;
			}


			if (!options.showPlaylistBut) {
				audio7_html5_showHidePlaylist_btn.css({
					'display':'none',
					'width':0,
					'height':0,
					'padding':0,
					'margin':0
				});
			}




			/*current_obj.timerTopPos=parseInt(audio7_html5_Audio_timer_a.css('top').substring(0, audio7_html5_Audio_timer_a.css('top').length-2),10);
			current_obj.timerLeftPos=parseInt(audio7_html5_Audio_timer_a.css('left').substring(0, audio7_html5_Audio_timer_a.css('left').length-2),10);*/

			audio7_html5_container.width(options.playerWidth);
			options.origWidth=options.playerWidth;









			if (options.sticky) {
					//audio7_html5_the_wrapper.addClass('audio7_html5_sticky');
					audio7_html5_the_wrapper.wrap("<div class='audio7_html5_sticky'></div>");
					current_obj.minimizeTopPos=(-1)*audio7_html5_minimize_btn.height();
					current_obj.minimizeLeftPos=current_obj.constantDistance;
				    audio7_html5_minimize_btn.css({
						'top':current_obj.minimizeTopPos+'px',
						'right':current_obj.minimizeLeftPos+'px'
					});
					audio7_html5_container.css({
						'padding':options.playerPadding+'px'
					});
					$('.audio7_html5_sticky').css({
						'background':options.playerBg
					});
					audio7_html5_minimize_btn.on( "click", function() {
							var animation_duration=500;
							/*if (current_obj.cookie_isMinified!=undefined && current_obj.cookie_isMinified=='true') {
								animation_duration=0;
							}*/
							var aux_display;
							var aux_visibility;
							var aux_bottom;
							if (current_obj.isMinified) {
								aux_bottom=1;
								current_obj.isMinified=false;
								audio7_html5_minimize_btn.removeClass('AudioOpenBut');
								aux_display='block';
								aux_visibility='visible'
							}  else {
								if (current_obj.isPlaylistVisible) {
									aux_bottom=(-1)*(current_obj.audioPlayerHeight+audio7_html5_thumbsHolderWrapper.height()+options.playlistTopPos);
								} else {
									aux_bottom=(-1)*current_obj.audioPlayerHeight;
								}
								current_obj.isMinified=true;
								audio7_html5_minimize_btn.addClass('AudioOpenBut');
								aux_display='none';
								aux_visibility='hidden'
							}

							setCookie(options,'cookie_isMinified', current_obj.isMinified);

							if (options.playerWidth<645) {
								aux_display='none';
								aux_visibility='hidden'
							}
							////if (options.playerWidth<645) {
									if (options.showVinylRecord) {
											audio7_html5_vinyl_record_on.css({
												'visibility':aux_visibility
											});
											audio7_html5_vinyl_record_off.css({
												'visibility':aux_visibility
											});
											if (!current_obj.isMinified) {
												if (document.getElementById(current_obj.audioID).paused) {
													if (options.playerWidth>645) {
															audio7_html5_vinyl_record_on.css({
																'visibility':'hidden',
																'display':'none'
															});
															audio7_html5_vinyl_record_off.css({
																'visibility':'visible',
																'display':'block'
															});
													}
												} else {
													if (options.playerWidth>645) {
															audio7_html5_vinyl_record_on.css({
																'visibility':'visible',
																'display':'block'
															});
															audio7_html5_vinyl_record_off.css({
																'visibility':'hidden',
																'display':'none'
															});
													}
												}
											}
									} else {
											audio7_html5_ximage.css({
													'display':aux_display
											});
									}
							////}

							$('.audio7_html5_sticky').animate({
								'bottom': aux_bottom+'px'
							}, animation_duration, 'easeOutQuad', function() {
								$('.audio7_html5_sticky').css({
									'left':'0px'
								});
								// Animation complete.
								//alert ("complete");
							});
					});
			} else {
				//initilize the player with the options
				audio7_html5_container.css({
					'background':options.playerBg,
					'padding':options.playerPadding+'px'
				});
				audio7_html5_minimize_btn.css({
					'display':'none'
				});
			}


			$('.bar', audio7_html5_container).css({
				'background':options.barsColor
			});


			arrangePlayerElements(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_innerSelectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_vinyl_record_on,audio7_html5_vinyl_record_off,audio7_html5_the_wrapper,audio7_html5_popup_btn,audio7_html5_the_bars,audio7_html5_volumeMute_btn,audio7_html5_volumeSlider,audio7_html5_shuffle_btn,audio7_html5_download_btn,audio7_html5_showHidePlaylist_btn,audio7_html5_lyrics_btn,audio7_html5_facebook_btn,audio7_html5_twitter_btn,audio7_html5_rewind_btn,audio7_html5_prev_btn,audio7_html5_next_btn,audio7_html5_buy_btn,audio7_html5_clearFavoritesBut);



			//generate playlist
			var currentCarouselTop=0;
			var audio7_html5_thumbsHolderWrapper = $('.thumbsHolderWrapper', audio7_html5_container);
			var audio7_html5_playlistPadding = $('.playlistPadding', audio7_html5_container);
			var audio7_html5_thumbsHolderVisibleWrapper = $('.thumbsHolderVisibleWrapper', audio7_html5_container);
			var audio7_html5_thumbsHolder = $('.thumbsHolder', audio7_html5_container);
			current_obj.audio7_html5_sliderVertical = $('.slider-vertical', audio7_html5_container);
			var audio7_html5_selectedCategDiv = $('.selectedCategDiv', audio7_html5_container);
			var audio7_html5_innerSelectedCategDiv = $('.innerSelectedCategDiv', audio7_html5_container);
			var audio7_html5_searchDiv = $('.searchDiv', audio7_html5_container);
			var audio7_html5_search_term = $('.search_term', audio7_html5_container);



			if (!options.showPlaylist) {
				//audio7_html5_thumbsHolderWrapper.css({'display':'none'});
				audio7_html5_thumbsHolderWrapper.css({
					'opacity':0,
					'visibility':'hidden'
				});
			}

			if (!options.showPlaylistOnInit) {
				audio7_html5_thumbsHolderWrapper.css({
					    'opacity': 0,
						'visibility':'hidden',
						'margin-top':'-20px'/*,
						'display':'none'*/
				});
				current_obj.isPlaylistVisible=false;
			}

			if (!options.showCategories)	{
				audio7_html5_selectedCategDiv.css({
					'visibility':'hidden',
					'height':0
				});
			}

			audio7_html5_selectedCategDiv.css({
				'background-color':options.selectedCategBg,
				'background-position':'10px 50%',
				'margin-bottom':options.selectedCategMarginBottom+'px'
			});
			audio7_html5_innerSelectedCategDiv.css({
				'color':options.selectedCategOffColor,
				'background-position':(options.playerWidth-2*options.playlistPadding-20)+'px 50%'
			});

			if (!options.showSearchArea)	{
				audio7_html5_searchDiv.css({
					'visibility':'hidden',
					'height':0
				});
			}


			audio7_html5_searchDiv.css({
				'background-color':options.searchAreaBg,
				'margin-top':options.selectedCategMarginBottom+'px'
			});

			audio7_html5_search_term.val(options.searchInputText);
			audio7_html5_search_term.css({
				'width':(options.playerWidth-30-2*options.playlistPadding-7)+'px', // 30 is the left-roght margins defined .css file
				'background-color':options.searchInputBg,
				'border-color':options.searchInputBorderColor,
				'color':options.searchInputTextColor
			});
			audio7_html5_thumbsHolderWrapper.css({
				'width':audio7_html5_container.width()+2*options.playerPadding+'px',
				'top':current_obj.audioPlayerHeight+options.playlistTopPos+'px',
				'left':'0px',
				'background':options.playlistBgColor

			});

			audio7_html5_thumbsHolderVisibleWrapper.width(audio7_html5_container.width());


			current_obj.playlist_arr=new Array();
			current_obj.category_arr=new Array();
			var resultsSplit_arr=new Array();

			var playlistElements = $('.xaudioplaylist', audio7_html5_container).children();
			var currentElement;
			playlistElements.each(function() { // ul-s
	            currentElement = $(this);
	            current_obj.total_images++;
	            current_obj.playlist_arr[current_obj.total_images-1]=new Array();
	            current_obj.playlist_arr[current_obj.total_images-1]['title']='';
	            current_obj.playlist_arr[current_obj.total_images-1]['author']='';
							current_obj.playlist_arr[current_obj.total_images-1]['authorlink']='';
	            current_obj.playlist_arr[current_obj.total_images-1]['image']='';
				current_obj.playlist_arr[current_obj.total_images-1]['category']='';
	            current_obj.playlist_arr[current_obj.total_images-1]['sources_mp3']='';
	            current_obj.playlist_arr[current_obj.total_images-1]['sources_ogg']='';
				current_obj.playlist_arr[current_obj.total_images-1]['buy_link']='';
				current_obj.playlist_arr[current_obj.total_images-1]['lyrics_link']='';

	            //alert (currentElement.find('.xtitle').html())
	            if (currentElement.find('.xtitle').html()!=null) {
	            	current_obj.playlist_arr[current_obj.total_images-1]['title']=currentElement.find('.xtitle').html();
	            }

	            if (currentElement.find('.xauthor').html()!=null) {
	            	current_obj.playlist_arr[current_obj.total_images-1]['author']=currentElement.find('.xauthor').html();
	            }

							if (currentElement.find('.xauthorlink').html()!=null) {
	            	current_obj.playlist_arr[current_obj.total_images-1]['authorlink']=currentElement.find('.xauthorlink').html();
	            }

							if (currentElement.find('.xauthorlinktarget').html()!=null) {
	            	current_obj.playlist_arr[current_obj.total_images-1]['authorlinktarget']=currentElement.find('.xauthorlinktarget').html();
	            }

	            if (currentElement.find('.ximage').html()!=null) {
	            	current_obj.playlist_arr[current_obj.total_images-1]['image']=currentElement.find('.ximage').html();
	            }

	            if (currentElement.find('.xbuy').html()!=null) {
	            	current_obj.playlist_arr[current_obj.total_images-1]['buy_link']=currentElement.find('.xbuy').html();
	            }

	            if (currentElement.find('.xlyrics').html()!=null) {
	            	current_obj.playlist_arr[current_obj.total_images-1]['lyrics_link']=currentElement.find('.xlyrics').html();
	            }

				if (currentElement.find('.xcategory').html()!=null) {
	            	current_obj.playlist_arr[current_obj.total_images-1]['category']=currentElement.find('.xcategory').html()+';';

				   resultsSplit_arr = current_obj.playlist_arr[current_obj.total_images-1]['category'].split(';');
				   for (var j=0;j<resultsSplit_arr.length;j++) {
					  if (current_obj.category_arr.indexOf(resultsSplit_arr[j])===-1 && resultsSplit_arr[j]!='') {
						  current_obj.category_arr.push(resultsSplit_arr[j]);
					  }
				   }
	            }


	            if (currentElement.find('.xsources_mp3').html()!=null) {
	            	current_obj.playlist_arr[current_obj.total_images-1]['sources_mp3']=currentElement.find('.xsources_mp3').html();
	            }

	            if (currentElement.find('.xsources_ogg').html()!=null) {
	            	current_obj.playlist_arr[current_obj.total_images-1]['sources_ogg']=currentElement.find('.xsources_ogg').html();
	            }

			});

			current_obj.cookie_firstCateg=getCookie(options,'cookie_firstCateg');
			if (current_obj.cookie_firstCateg!=undefined) {
				options.firstCateg=current_obj.cookie_firstCateg;
			}

			current_obj.numberOfCategories=current_obj.category_arr.length;
			current_obj.category_arr.sort();
			//alert ("firstCateg:"+options.firstCateg);
			current_obj.selectedCateg=options.firstCateg;
			if (options.firstCateg=='' && current_obj.category_arr.indexOf(options.firstCateg)===-1) {
				current_obj.selectedCateg=current_obj.category_arr[0];
			}
			audio7_html5_innerSelectedCategDiv.html(current_obj.selectedCateg);
            //generate playlist for the first time
			generatePlaylistByCateg(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_the_wrapper,audio7_html5_next_btn,audio7_html5_rewind_btn);


			if (options.showClearFavoritesBut) {
					audio7_html5_clearFavoritesBut.on( "click", function() {
							setCookieFav(options, 'cookie_favorites'+current_obj.instanceID, '', options.favoritesCookieExpirationDays);
							current_obj.cookie_favorites_arr.splice(0,current_obj.cookie_favorites_arr.length);

							generatePlaylistByCateg(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_the_wrapper,audio7_html5_next_btn,audio7_html5_rewind_btn);
					});
			}

//alert (audio7_html5_container.css("top"));



			//selectedCategDiv
			audio7_html5_selectedCategDiv.on( "click", function() {
				current_obj.search_val='';
			    audio7_html5_search_term.val(options.searchInputText);

				generateCategories(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_innerSelectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_the_wrapper,audio7_html5_next_btn,audio7_html5_rewind_btn);
			});



			audio7_html5_selectedCategDiv.on( "mouseover", function() {
				audio7_html5_innerSelectedCategDiv.css({
					'color':options.selectedCategOnColor
				});
			});


			audio7_html5_selectedCategDiv.on( "mouseout", function() {
				audio7_html5_innerSelectedCategDiv.css({
					'color':options.selectedCategOffColor
				});
			});





			current_obj.cookie_initialVolume=getCookie(options,'cookie_initialVolume');
			if (current_obj.cookie_initialVolume) {
				options.initialVolume=current_obj.cookie_initialVolume;
			}
			//start initialize volume slider
			audio7_html5_volumeSlider.slider({
				value: options.initialVolume,
				step: 0.05,
				orientation: "vertical",
				range: "min",
				min: 0,
				max: 1,
				animate: true,
				slide:function(e,ui){
						//document.getElementById(current_obj.audioID).muted=false;
						document.getElementById(current_obj.audioID).volume=ui.value;
						setCookie(options,'cookie_initialVolume', ui.value);
				},
				stop:function(e,ui){

				}
			});
			document.getElementById(current_obj.audioID).volume=options.initialVolume;
			audio7_html5_volumeSlider.css({'background':options.volumeOffColor});
			$(".ui-widget-header",audio7_html5_volumeSlider).css({'background':options.volumeOnColor});
			//end initialize volume slider



			//buttons start
			audio7_html5_play_btn.on( "click", function() {
				var is_paused=document.getElementById(current_obj.audioID).paused;
				cancelAll(options,audio7_html5_container);
				if (is_paused == false) {
					document.getElementById(current_obj.audioID).pause();
					audio7_html5_play_btn.removeClass('AudioPause');
					$('#changePlaylist_'+current_obj.selectedCateg).removeClass('external_pause_button');
					manageCss3Animations(options,current_obj,audio7_html5_container,false);
					setCookie(options,'cookie_autoPlay', false);
				} else {
					document.getElementById(current_obj.audioID).play();
					audio7_html5_play_btn.addClass('AudioPause');
					$('#changePlaylist_'+current_obj.selectedCateg).addClass('external_pause_button');
					manageCss3Animations(options,current_obj,audio7_html5_container,true);
					setCookie(options,'cookie_autoPlay', true);
				}
			});

			audio7_html5_rewind_btn.on( "click", function() {
				document.getElementById(current_obj.audioID).currentTime=0;
				cancelAll(options,audio7_html5_container);
				document.getElementById(current_obj.audioID).play();
				audio7_html5_play_btn.addClass('AudioPause');
				manageCss3Animations(options,current_obj,audio7_html5_container,true);
				//alert (document.getElementById(current_obj.audioID).playing);
			});

			audio7_html5_next_btn.on( "click", function() {
				if (!current_obj.categsAreListed) {
					if (!current_obj.is_changeSrc || current_obj.is_very_first) {
						options.autoPlay=true;
						//$(current_obj.thumbsHolder_Thumbs[current_obj.current_img_no]).removeClass('thumbsHolder_ThumbON');
						var my_bgColor="transparent";
						if (options.playlistRecordBgOffColor!='#') {
							my_bgColor=options.playlistRecordBgOffColor;
						}
						current_obj.thumbsHolder_Thumbs.css({
							"background":my_bgColor,
							"border-bottom-color":options.playlistRecordBottomBorderOffColor,
							"color":options.playlistRecordTextOffColor
						});

						manageCss3Animations(options,current_obj,audio7_html5_container,true);
						findNextVideoNumbers(current_obj,options,'next');

						if (options.continuouslyPlayOnAllPages) {
								setCookie(options,'cookie_current_img_no', current_obj.current_img_no);
								setCookie(options,'cookie_origID', current_obj.origID);
						}

						changeSrc(current_obj,options,audio7_html5_thumbsHolder,audio7_html5_container,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_rewind_btn);
					}
				}
			});

			audio7_html5_prev_btn.on( "click", function() {
				if (!current_obj.categsAreListed) {
					if (!current_obj.is_changeSrc || current_obj.is_very_first) {
						options.autoPlay=true;
						//$(current_obj.thumbsHolder_Thumbs[current_obj.current_img_no]).removeClass('thumbsHolder_ThumbON');
						var my_bgColor="transparent";
						if (options.playlistRecordBgOffColor!='#') {
							my_bgColor=options.playlistRecordBgOffColor;
						}
						current_obj.thumbsHolder_Thumbs.css({
							"background":my_bgColor,
							"border-bottom-color":options.playlistRecordBottomBorderOffColor,
							"color":options.playlistRecordTextOffColor
						});

						manageCss3Animations(options,current_obj,audio7_html5_container,true);
						findNextVideoNumbers(current_obj,options,'previous');

						if (options.continuouslyPlayOnAllPages) {
								setCookie(options,'cookie_current_img_no', current_obj.current_img_no);
								setCookie(options,'cookie_origID', current_obj.origID);
						}

						changeSrc(current_obj,options,audio7_html5_thumbsHolder,audio7_html5_container,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_rewind_btn);
					}
				}
			});


			audio7_html5_showHidePlaylist_btn.on( "click", function() {
				var aux_opacity;
				var aux_display;
				var aux_margin_top;
				var aux_height;

				audio7_html5_thumbsHolderWrapper.css({
						'visibility':'visible'
				});
				if (audio7_html5_thumbsHolderWrapper.css('margin-top').substring(0, audio7_html5_thumbsHolderWrapper.css('margin-top').length-2) < 0) {
					aux_opacity=1;
					aux_display='block';
					current_obj.isPlaylistVisible=true;
					aux_margin_top="0px";
					aux_height=current_obj.audioPlayerHeight+audio7_html5_thumbsHolderWrapper.height()+options.playlistTopPos;
					audio7_html5_thumbsHolderWrapper.css({
						'display':aux_display
					});
					if (current_obj.selectedCateg_total_images>options.numberOfThumbsPerScreen)
						current_obj.audio7_html5_sliderVertical.css({
							'opacity': 1,
							'display':'block'
						});
				} else {
					aux_opacity=0;
					aux_display='none';
					current_obj.isPlaylistVisible=false;
					aux_margin_top="-20px";
					if (current_obj.selectedCateg_total_images>options.numberOfThumbsPerScreen)
						current_obj.audio7_html5_sliderVertical.css({
							'opacity': 0,
							'display':'none'
						});
					aux_height=current_obj.audioPlayerHeight;
				}

				audio7_html5_thumbsHolderWrapper.animate({
					    'opacity': aux_opacity,
						'margin-top':aux_margin_top

					  }, 500, 'easeOutQuad', function() {
					    // Animation complete.
						audio7_html5_thumbsHolderWrapper.css({
							'display':aux_display
						});
					});

				audio7_html5_the_wrapper.animate({
					    'height': aux_height
					  }, 500, 'easeOutQuad', function() {
					    // Animation complete.
					});

			});

			audio7_html5_volumeMute_btn.on( "click", function() {
				if (!document.getElementById(current_obj.audioID).muted) {
					document.getElementById(current_obj.audioID).muted=true;
					audio7_html5_volumeMute_btn.addClass('VolumeButtonMuted');
					setCookie(options,'cookie_muteVolume', 1);
				} else {
					document.getElementById(current_obj.audioID).muted=false;
					audio7_html5_volumeMute_btn.removeClass('VolumeButtonMuted');
					setCookie(options,'cookie_muteVolume', 0);
				}
			});


			audio7_html5_shuffle_btn.on( "click", function() {
				if (options.shuffle) {
					audio7_html5_shuffle_btn.removeClass('AudioShuffleON');
					options.shuffle=false;
					setCookie(options,'cookie_shuffle', false);
				} else {
					audio7_html5_shuffle_btn.addClass('AudioShuffleON');
					options.shuffle=true;
					setCookie(options,'cookie_shuffle', true);
				}
			});

			audio7_html5_download_btn.on( "click", function() {
				//alert (current_obj.playlist_arr[current_obj.origID]['sources_mp3']);
				window.open(options.pathToDownloadFile+"download.php?the_file="+current_obj.playlist_arr[current_obj.origID]['sources_mp3']);
			});

			audio7_html5_buy_btn.on( "click", function() {
				if (current_obj.playlist_arr[current_obj.origID]['buy_link']!='') {
					if (options.buyButTarget=="_blank")
						window.open(current_obj.playlist_arr[current_obj.origID]['buy_link']);
					else
						window.location = current_obj.playlist_arr[current_obj.origID]['buy_link'];
				} else {
					alert ("no link defined");
				}
			});

			audio7_html5_lyrics_btn.on( "click", function() {
				if (current_obj.playlist_arr[current_obj.origID]['lyrics_link']!='') {
					if (options.lyricsButTarget=="_blank")
						window.open(current_obj.playlist_arr[current_obj.origID]['lyrics_link']);
					else
						window.location = current_obj.playlist_arr[current_obj.origID]['lyrics_link'];
				} else {
					alert ("no link defined");
				}
			});


			//facebook
			if (options.showFacebookBut) {
					  window.fbAsyncInit = function() {
						FB.init({
						  appId:options.facebookAppID,
						  version:'v3.2',
						  status:true,
						  cookie:true,
						  xfbml:true
						});
					  };

					  (function(d, s, id){
						 var js, fjs = d.getElementsByTagName(s)[0];
						 if (d.getElementById(id)) {return;}
						 js = d.createElement(s); js.id = id;
						 js.src = "//connect.facebook.com/en_US/sdk.js";
						 fjs.parentNode.insertBefore(js, fjs);
					   }(document, 'script', 'facebook-jssdk'));

						audio7_html5_facebook_btn.on( "click", function() {
							var imageLink=current_obj.playlist_arr[current_obj.origID]['image'];
							var pathArray = window.location.pathname.split( '/' );
							if (imageLink.indexOf('http://')!=-1 || imageLink.indexOf('https://')!=-1) {
								//imageLink=current_obj.playlist_arr[current_obj.origID]['image'];
							} else {
								if (pathArray[pathArray.length-1].indexOf('.')!=-1) {
									pathArray.pop();
								}
								imageLink=window.location.protocol+'//'+window.location.host+'/'+pathArray.join('/')+'/'+current_obj.playlist_arr[current_obj.origID]['image'];
							}
							/*alert (imageLink+'  --  '+ current_obj.playlist_arr[current_obj.origID]['image']+'  --  '+current_obj.origID+'  --  '+current_obj.playlist_arr[current_obj.origID]['title']);*/


							//USING FaceBook API

							/*FB.ui(
								{
							   method: 'feed',
							   href: document.URL
							  },
							  function(response) {
								//if (response && response.post_id) {
								  //alert('Post was published.');
								//} else {
								 // alert('Post was not published.');
								//}
							  }
							);*/


							//USING FaceBook feed
							//window.open("https://www.facebook.com/dialog/feed?app_id="+options.facebookAppID+"&display=popup&caption=An%20example%20caption &link=http://stickyfullwidth.audioplayerhtml5.com/standard-white-buttons/&redirect_uri=http://stickyfullwidth.audioplayerhtml5.com/standard-white-buttons/","Facebook","status = 1, left = 430, top = 270, height = 550, width = 420, resizable = 0");
							//window.open("https://www.facebook.com/dialog/feed?app_id="+options.facebookAppID+"&display=popup&caption=An%20example%20caption &link="+document.URL,"Facebook","status = 1, left = 430, top = 270, height = 550, width = 420, resizable = 0");
							FB.ui({
								method: 'share_open_graph',
								//method: 'share',
								action_type: 'og.likes',
								//action_type: 'og.shares',
								action_properties: JSON.stringify({
									object: {
										'og:url': document.URL,
										'og:title': options.facebookShareTitle,
										'og:description': options.facebookShareDescription,
										'og:image': imageLink
									}
								})
							},
							function (response) {
								// Action after response
							});

						});
				}



			//twitter
			if (options.showTwitterBut) {
				audio7_html5_twitter_btn.on( "click", function() {
					//var myURL = "http://www.google.com";
					window.open("https://twitter.com/intent/tweet?url=" + document.URL+ "&text="+current_obj.playlist_arr[current_obj.origID]['title'],"Twitter","status = 1, left = 430, top = 270, height = 550, width = 420, resizable = 0");
				});
			}

			//popup
			if (options.showPopupBut) {
				audio7_html5_popup_btn.on( "click", function() {
					//alert (location.href);
					clearInterval(current_obj.timeupdateInterval);
					audio7_html5_the_wrapper[0].innerHTML="";
					if (options.sticky) {
						$('.audio7_html5_sticky')[0].innerHTML="";
					}
					current_obj.cookie_popupWin=window.open(location.href, 'audio7_PopupName', 'width='+options.popupWidth+', height='+options.popupHeight+', left=24, top=24, scrollbars=no, resizable');
					current_obj.cookie_popupWin.focus();
					setCookie(options,'cookie_popupWin', current_obj.cookie_popupWin,1201);
					/*return false;*/
					//audio7_html5_the_wrapper.css({'display':'none'});

				});
			}
			//buttons end



			audio7_html5_thumbsHolder.swipe( {
				swipeStatus:function(event, phase, direction, distance, duration, fingerCount)
				{
					//$('#logulmeu').html("phase: "+phase+"<br>direction: "+direction+"<br>distance: "+distance);
					var currentScrollVal;
					if (current_obj.selectedCateg_total_images>options.numberOfThumbsPerScreen) {
									if (direction=='up' || direction=='down') {
										if (distance!=0) {
											  currentScrollVal=current_obj.audio7_html5_sliderVertical.slider( "value");
											  if (direction=="up") {
													currentScrollVal = currentScrollVal - 1.5;
											  } else {
													currentScrollVal = currentScrollVal + 1.5;
											  }
											  current_obj.audio7_html5_sliderVertical.slider( "value", currentScrollVal);
												//page scroll enabled
												$('html, body')
								            // Needed to remove previously bound handlers
								            .off('touchstart touchmove')
								            .on('touchstart touchmove', function (e) {
								                e.preventDefault();
								            });
												//page scroll enabled
											  carouselScroll(currentScrollVal,current_obj,options,audio7_html5_thumbsHolder);
										}
									}
					}

				  //Here we can check the:
				  //phase : 'start', 'move', 'end', 'cancel'
				  //direction : 'left', 'right', 'up', 'down'
				  //distance : Distance finger is from initial touch point in px
				  //duration : Length of swipe in MS
				  //fingerCount : the number of fingers used
				  },

				  threshold:100,
				  maxTimeThreshold:500,
				  fingers:'all'
			});







			$.audio7_html5.destroyPlayerInstance = function(the_instance_id) {
				alert (the_param);
			}

			$.audio7_html5.changeMp3 = function(the_mp3,the_title,the_author,the_img) {
								current_obj.totalTime = 'Infinity';
								//seekbar init
								if (options.isSliderInitialized) {
									audio7_html5_Audio_seek.slider("destroy");
									options.isSliderInitialized=false;
								}
								if (options.isProgressInitialized) {
									audio7_html5_Audio_buffer.progressbar("destroy");
									options.isProgressInitialized=false;
								}
								current_obj.is_changeSrc=true;
								current_obj.is_buffer_complete=false;

								audio7_html5_Title.width(current_obj.titleWidth);
								audio7_html5_Author.width(current_obj.titleWidth);
								audio7_html5_Audio_buffer.css({'background':options.bufferEmptyColor});


								current_obj.curSongText='';

								if (options.showTitle && the_title!=null && the_title!='') {
												current_obj.curSongText+=the_title;
											}
								current_obj.isAuthorTitleInsideScrolling=false;
								current_obj.authorTitleInsideWait=0;
								audio7_html5_TitleInside.stop();
								audio7_html5_TitleInside.css({'margin-left':0});
								audio7_html5_TitleInside.html(current_obj.curSongText);

								if (options.showAuthor && the_author!=null && the_author!='') {
									audio7_html5_Author.html(the_author);
								}

								audio7_html5_ximage.html('<img src="'+the_img+'" width="149">');

								if (!current_obj.curSongText) {
									audio7_html5_Title.css({
										'display':'none',
										'width':0,
										'height':0,
										'padding':0,
										'margin':0
									});
								}


								var the_file=the_mp3;
								document.getElementById(current_obj.audioID).src=the_file;
								document.getElementById(current_obj.audioID).load();

								/*if (val.indexOf("android") != -1) {
									//nothing
								} else if ((val.indexOf("ipad") != -1 || val.indexOf("iphone") != -1 || val.indexOf("ipod") != -1 || val.indexOf("webos") != -1) && current_obj.is_very_first) {
									//nothing
								} else {
									if (options.autoPlay) {*/
										cancelAll(options,audio7_html5_container);
										document.getElementById(current_obj.audioID).play();
										//audio7_html5_play_btn.click();
										audio7_html5_play_btn.addClass('AudioPause');
										manageCss3Animations(options,current_obj,audio7_html5_container,true);
									/*} else {
										audio7_html5_play_btn.removeClass('AudioPause');
										manageCss3Animations(options,current_obj,audio7_html5_container,false);
									}
								}*/
			}


			$.audio7_html5.changePlaylist = function(the_categ_name) {
							var theTimeOut=100;
							if (current_obj.isMinified) {
									audio7_html5_minimize_btn.click();
									theTimeOut=800;
							}
							if (current_obj.selectedCateg==the_categ_name) {
									if (audio7_html5_play_btn.hasClass('AudioPause')) {
												$('#changePlaylist_'+current_obj.selectedCateg).removeClass('external_pause_button');
									}	else {
												$('#changePlaylist_'+current_obj.selectedCateg).addClass('external_pause_button');
									}
									audio7_html5_play_btn.click();
							} else {
									setTimeout(function(){
											//options.showPlaylist=false;
											options.showPlaylistOnInit=false;
											//hide playlist
											audio7_html5_thumbsHolderWrapper.css({
												   'opacity': 0,
													'visibility':'hidden',
													'margin-top':'-20px'/*,
													'display':'none'*/
											});
											current_obj.isPlaylistVisible=false;

											current_obj.changedCategFromApi=true;

											current_obj.selectedCateg=the_categ_name;
											setCookie(options,'cookie_firstCateg', current_obj.selectedCateg);
											audio7_html5_innerSelectedCategDiv.html(current_obj.selectedCateg);
											generatePlaylistByCateg(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_the_wrapper,audio7_html5_next_btn,audio7_html5_rewind_btn);
									}, theTimeOut);
						 }
			}


			//search area functions
			audio7_html5_search_term.on('click', function() {
				$(this).val('');
			});
			audio7_html5_search_term.on('input', function() {
				//alert( $(this).val() );
				current_obj.search_val=audio7_html5_search_term.val().toLowerCase();
				generatePlaylistByCateg(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_the_wrapper,audio7_html5_next_btn,audio7_html5_rewind_btn);
			});



			//audio ended
			document.getElementById(current_obj.audioID).addEventListener('ended',function (){endAudioHandler(current_obj,options,audio7_html5_container,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_next_btn,audio7_html5_Audio)
			});

			//google analytics
			if (options.googleTrakingOn) {
				ga('create', options.googleTrakingCode, 'auto');
			}


			//initialize first Audio
			current_obj.cookie_timePlayed=getCookie(options,'cookie_timePlayed');
			current_obj.cookie_current_img_no=getCookie(options,'cookie_current_img_no');
			current_obj.cookie_origID=getCookie(options,'cookie_origID');
			current_obj.cookie_autoPlay=getCookie(options,'cookie_autoPlay');
			current_obj.cookie_shuffle=getCookie(options,'cookie_shuffle');
			current_obj.cookie_isMinified=getCookie(options,'cookie_isMinified');
			//current_obj.cookie_firstCateg=getCookie(options,'cookie_firstCateg');  /moved up where the categs are generated
			//alert (current_obj.cookie_current_img_no);
			if (current_obj.cookie_current_img_no!=undefined) {
				current_obj.current_img_no=current_obj.cookie_current_img_no;
				if (current_obj.cookie_origID!=undefined) {
					current_obj.origID=current_obj.cookie_origID;
				}
			}/* else {
				if (options.shuffle) {
					getRandomNumber(options,current_obj);
					audio7_html5_shuffle_btn.addClass('AudioShuffleON');
				}
			}*/
			if (options.continuouslyPlayOnAllPages) {
				setCookie(options,'cookie_current_img_no', current_obj.current_img_no);
			}
			if (current_obj.cookie_autoPlay!=undefined) {
				if (current_obj.cookie_autoPlay=='true')
					options.autoPlay=true;
				else
					options.autoPlay=false;
				//alert ("if: "+current_obj.cookie_autoPlay+'  -  '+options.autoPlay+'  -  '+current_obj.cookie_timePlayed);
			} else {
				//alert ("else: "+current_obj.cookie_autoPlay+'  -  '+options.autoPlay+'  -  '+current_obj.cookie_timePlayed);
			}


			//chrome and safari on mac auto-play restrictions 2018 start
			//alert (current_obj.cookie_autoPlay);
			if (current_obj.cookie_autoPlay!='true') {
						//alert (navigator.vendor+'  ---  '+navigator.platform+'  ---  '+navigator.userAgent);
						if ((navigator.userAgent.indexOf("Opera")==-1 &&  navigator.userAgent.indexOf('OPR')) == -1  ) {  // is NOT Opera
									if (navigator.userAgent.indexOf("Chrome")!=-1 && navigator.vendor.indexOf('Google')!=-1 ) { //is chrome
											options.autoPlay=false;
											//alert ('is chrome');
									}
									if (navigator.userAgent.indexOf("Safari")!=-1 && navigator.vendor.indexOf('Apple')!=-1 && navigator.platform.indexOf('Win')==-1) { //is safari on mac
										options.autoPlay=false;
										//alert ('is safari');
									}
						}
			}
			//chrome and safari on mac auto-play restrictions 2018 end


			if (current_obj.cookie_shuffle!=undefined) {
				if (current_obj.cookie_shuffle=='true') {
					options.shuffle=true;
					audio7_html5_shuffle_btn.addClass('AudioShuffleON');
				} else {
					options.shuffle=false;
					audio7_html5_shuffle_btn.removeClass('AudioShuffleON');
				}
				//alert ("if: "+current_obj.cookie_shuffle+'  -  '+options.shuffle);
			} else {
				//alert ("else: "+current_obj.cookie_shuffle+'  -  '+options.shuffle);
			}
			if (current_obj.cookie_timePlayed) {
					document.getElementById(current_obj.audioID).currentTime=current_obj.cookie_timePlayed;
					//alert (document.getElementById(current_obj.audioID).currentTime);
					current_obj.cookie_timePlayed=null;
			}
			/*alert ("cookie_firstCateg:"+current_obj.cookie_firstCateg);
			if (current_obj.cookie_firstCateg!=undefined) {
				options.firstCateg=current_obj.cookie_firstCateg;
			}*/

			if (options.shuffle && current_obj.cookie_current_img_no!=undefined ) {
					current_obj.is_very_first=false;
			}


			changeSrc(current_obj,options,audio7_html5_thumbsHolder,audio7_html5_container,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_rewind_btn);

			current_obj.cookie_muteVolume=getCookie(options,'cookie_muteVolume');
			if (current_obj.cookie_muteVolume>=1) {
				audio7_html5_volumeMute_btn.click();
			}

			current_obj.timeupdateInterval=setInterval(function(){
					//alert (document.getElementById(current_obj.audioID).currentTime);
					seekUpdate(current_obj,options,audio7_html5_container,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_play_btn,audio7_html5_Audio,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author);
    		},300);

			document.getElementById(current_obj.audioID).addEventListener("durationchange", function() {
				if (current_obj.is_changeSrc) {
					current_obj.totalTime = document.getElementById(current_obj.audioID).duration;
				}
			});

			if (val.indexOf("ipad") != -1 || val.indexOf("iphone") != -1 || val.indexOf("ipod") != -1 || val.indexOf("webos") != -1) {
				if (current_obj.cookie_timePlayed) {
					document.getElementById(current_obj.audioID).currentTime=current_obj.cookie_timePlayed;
					//alert (document.getElementById(current_obj.audioID).currentTime);
					current_obj.cookie_timePlayed=null;
				}

				current_obj.totalTime=0;
				document.getElementById(current_obj.audioID).addEventListener("canplaythrough", function() {
					if (current_obj.totalTime != document.getElementById(current_obj.audioID).duration) {
						//seekbar init
						if (options.isSliderInitialized) {
							audio7_html5_Audio_seek.slider("destroy");
							options.isSliderInitialized=false;
						}
						if (options.isProgressInitialized) {
							audio7_html5_Audio_buffer.progressbar("destroy");
							options.isProgressInitialized=false;
						}

						current_obj.totalTime = document.getElementById(current_obj.audioID).duration;
						generate_seekBar(current_obj,options,audio7_html5_container,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_play_btn,audio7_html5_Audio);
						if (options.isProgressInitialized) {
							audio7_html5_Audio_buffer.progressbar({ value: options.playerWidth });
						}
					}
				});
			}



			//////////////if (current_obj.cookie_isMinified!=undefined && current_obj.cookie_isMinified=='true' && !options.is_lbgSite) {
				////////////var autoMinimizePlayer=function () {
					//////////////////audio7_html5_minimize_btn.click();
					//alert ($('.audio7_html5_sticky').css('left'));
				/////////////////}
				//////////////////////$('.audio7_html5_sticky').css({
					///////////////'left':'-5000px'
				////////////////////});
				/*current_obj.isMinified=false;
				audio7_html5_minimize_btn.click();*/
				/////////////////////////////////////////////setTimeout(autoMinimizePlayer, 800);
				/*audio7_html5_vinyl_record_on.css({
					'visibility':'hidden',
					'display':'none'
				});
				audio7_html5_vinyl_record_off.css({
					'visibility':'hidden',
					'display':'none'
				});*/
			//////////////}


			//minimize player on startup
			if (options.sticky) {
				/*var autoMinimizePlayer=function () {
								audio7_html5_minimize_btn.click();
				}*/
				current_obj.cookie_isMinified=getCookie(options,'cookie_isMinified');
				if ((options.startMinified && current_obj.cookie_isMinified==undefined && !options.is_lbgSite) || (current_obj.cookie_isMinified!=undefined && current_obj.cookie_isMinified=='true' && !options.is_lbgSite)) {
						$('.audio7_html5_sticky').css({
							'left':'-5000px'
						});
						setTimeout(function(){
								audio7_html5_minimize_btn.click();
						}, 800);
						//setTimeout(autoMinimizePlayer, 800);
				}
			}



			var doResize = function() {
				  if (current_obj.origParentFloat=='') {
					  current_obj.origParentFloat=audio7_html5_container.parent().css('float');
					  current_obj.origParentPaddingTop=audio7_html5_container.parent().css('padding-top');
					  current_obj.origParentPaddingRight=audio7_html5_container.parent().css('padding-right');
					  current_obj.origParentPaddingBottom=audio7_html5_container.parent().css('padding-bottom');
					  current_obj.origParentPaddingLeft=audio7_html5_container.parent().css('padding-left');
				  }

				  //alert (options.playerWidth+'  !=    '+options.origWidth +'   ||   '+options.playerWidth+'   >    '+$(window).width());

				  /*if (options.playerWidth!=options.origWidth || options.playerWidth>$(window).width()) {
						  audio7_html5_container.parent().css({
							  'float':'none',
							  'padding-top':0,
							  'padding-right':0,
							  'padding-bottom':0,
							  'padding-left':0
						  });
				  } else {
					  audio7_html5_container.parent().css({
						  'float':current_obj.origParentFloat,
						  'padding-top':current_obj.origParentPaddingTop,
						  'padding-right':current_obj.origParentPaddingRight,
						  'padding-bottom':current_obj.origParentPaddingBottom,
						  'padding-left':current_obj.origParentPaddingLeft
					  });
				  }		*/
					/*audio7_html5_container.parent().css({
						  'float':'none'
					  });*/

				  var responsiveWidth=audio7_html5_container.parent().parent().width();
				  if (options.sticky) {
				  	responsiveWidth=$(window).width()
				  }
				  //var responsiveWidth=audio7_html5_container.parent().width();
				  var new_buffer_width;
				  //var responsiveHeight=audio7_html5_container.parent().height();



				  /*if (options.responsiveRelativeToBrowser) {
					  responsiveWidth=$(window).width();
					  responsiveHeight=$(window).height();
				  }*/


					//AAAA//
					/**********if (options.playerWidth<385) {
						  audio7_html5_volumeSlider.css ({
							  'display':'none'
						  });
					} else {
						  audio7_html5_volumeSlider.css ({
							  'display':'block'
						  });
					}

					if (options.playerWidth<270) {
						  audio7_html5_volumeMute_btn.css ({
							  'display':'none'
						  });
					} else {
						  audio7_html5_volumeMute_btn.css ({
							  'display':'block'
						  });
					}

					if (options.playerWidth<240) {
						 audio7_html5_next_btn.css ({
							  'display':'none'
						  });
					} else {
						  audio7_html5_next_btn.css ({
							  'display':'block'
						  });
					}***********/
					//AAAA//



					//alert (audio7_html5_container.width()+  '  !=   '+responsiveWidth);
					if (audio7_html5_container.width()!=responsiveWidth) {
						  //alert (audio7_html5_container.width()+"!="+responsiveWidth);

						  /*if (options.origWidth>responsiveWidth) {
							  options.playerWidth=responsiveWidth;
						  } else {
							  options.playerWidth=options.origWidth;
						  }*/

						  options.playerWidth=responsiveWidth;
						  if (options.sticky) {
							  if (options.origWidth>responsiveWidth) {
								  options.playerWidth=responsiveWidth;
							  } else {
								  options.playerWidth=options.origWidth;
							  }
						  }

						   //HIDE THE BARS
						  //if (options.playerWidth<options.origWidth) {
						 /******* if (options.playerWidth<current_obj.stickyFixedPlayerWidth) {
								audio7_html5_the_bars.css ({
									'display':'none'
								});
						  } else {
								audio7_html5_the_bars.css ({
									'display':'block'
								});
						  }*************/

						  //HIDE THE IMAGE
						 /***** if (options.playerWidth<645) {
								if (options.showVinylRecord) {
										audio7_html5_vinyl_record_on.css({
											'display':'none'
										});
										audio7_html5_vinyl_record_off.css({
											'display':'none'
										});
								} else {
										audio7_html5_ximage.css({
											'display':'none'
										});
								}
								////current_obj.playLeftPos=current_obj.imageLeftPos; // it is also defined in arrangePlayerElements() function
						  } else {
								if (options.showVinylRecord) {
										audio7_html5_vinyl_record_on.css({
											'display':'block'
										});
										audio7_html5_vinyl_record_off.css({
											'display':'block'
										});
								} else {
										audio7_html5_ximage.css({
											'display':'block'
										});
								}
								/////current_obj.playLeftPos=current_obj.imageLeftPos+audio7_html5_ximage.width()+24; // it is also defined in arrangePlayerElements() function
						  }

						 //HIDE THE TITLE & AUTHOR
						 if (options.playerWidth<460) {
								audio7_html5_Author.css({
									'display':'none'
								});

								audio7_html5_Title.css({
									'display':'none'
								});
						 } else {
							 	if (options.showAuthor) {
									audio7_html5_Author.css({
										'display':'block'
									});
								}
								if (options.showTitle) {
									audio7_html5_Title.css({
										'display':'block'
									});
								}
						 }

						 //HIDE THE BUFFER & TIMER
						 if (options.playerWidth<395) {
								audio7_html5_Audio_seek.css({
									'display':'none'
								});

								audio7_html5_Audio_buffer.css({
									'display':'none'
								});

								audio7_html5_Audio_timer_a.css({
									'margin-left':'7px'
								});

								audio7_html5_Audio_timer_b.css({
									'display':'none'
								});
						 } else {
								audio7_html5_Audio_seek.css({
									'display':'block'
								});

								audio7_html5_Audio_buffer.css({
									'display':'block'
								});

								audio7_html5_Audio_timer_a.css({
									'margin-left':'0px'
								});

								audio7_html5_Audio_timer_b.css({
									'display':'block'
								});
						 }

						 //HIDE VOLUME
						 if (options.playerWidth<345) {
								audio7_html5_volumeMute_btn.css({
									'display':'none'
								});

								audio7_html5_volumeSlider.css({
									'display':'none'
								});
						 } else {
								audio7_html5_volumeMute_btn.css({
									'display':'block'
								});

								audio7_html5_volumeSlider.css({
									'display':'block'
								});
						 }***********/


						  arrangePlayerElements(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_innerSelectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_vinyl_record_on,audio7_html5_vinyl_record_off,audio7_html5_the_wrapper,audio7_html5_popup_btn,audio7_html5_the_bars,audio7_html5_volumeMute_btn,audio7_html5_volumeSlider,audio7_html5_shuffle_btn,audio7_html5_download_btn,audio7_html5_showHidePlaylist_btn,audio7_html5_lyrics_btn,audio7_html5_facebook_btn,audio7_html5_twitter_btn,audio7_html5_rewind_btn,audio7_html5_prev_btn,audio7_html5_next_btn,audio7_html5_buy_btn,audio7_html5_clearFavoritesBut);


 						  //alert(audio7_html5_container.width()+' -- '+responsiveWidth+' -- '+options.playerWidth);
						  if (audio7_html5_container.width()!=options.playerWidth) {
						  		 audio7_html5_container.width(options.playerWidth);
								 audio7_html5_the_wrapper.width(options.playerWidth);

//generate_seekBar(current_obj,options,audio7_html5_container,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_play_btn,audio7_html5_Audio);
								  //new_buffer_width
								  current_obj.bufferWidth=options.playerWidth-current_obj.timerLeftPos-2*audio7_html5_Audio_timer_a.width()-2*current_obj.seekBarLeftRightSpacing-2*options.playerPadding-260-20; //260 the right buttons area including volume slider ; 20 the space between the right timer & volume
								  //alert (options.playerPadding+'  --  '+current_obj.bufferWidth);
								  audio7_html5_Audio_buffer.width(current_obj.bufferWidth);
								  audio7_html5_Audio_seek.width(current_obj.bufferWidth);


								  /****current_obj.titleWidth=options.playerWidth-current_obj.timerLeftPos-audio7_html5_ximage.width()-2*current_obj.constantDistance;

								  audio7_html5_Title.width(current_obj.titleWidth);
								  audio7_html5_Author.width(current_obj.titleWidth);****/


								  audio7_html5_thumbsHolderWrapper.width(audio7_html5_container.width()+2*options.playerPadding);
								  audio7_html5_thumbsHolderVisibleWrapper.width(audio7_html5_container.width())
								  //audio7_html5_thumbsHolder.width(audio7_html5_container.width()+2*options.playerPadding);
								  //audio7_html5_playlistPadding.css({'padding':options.playlistPadding+'px'});

								  //current_obj.thumbsHolder_Thumbs.width(audio7_html5_container.width()-2*options.playlistPadding);


								  audio7_html5_selectedCategDiv.width(options.playerWidth-2*options.playlistPadding);
								  audio7_html5_innerSelectedCategDiv.css({
									  'background-position':(options.playerWidth-2*options.playlistPadding-20)+'px 50%'
								  });


								  //the playlist elements
								  if (current_obj.selectedCateg_total_images>options.numberOfThumbsPerScreen && options.showPlaylist) {
									  current_obj.audio7_html5_sliderVertical.css({
										  'left':audio7_html5_container.width()+2*options.playerPadding-current_obj.audio7_html5_sliderVertical.width()-options.playlistPadding+'px'						  							  });
									  $('.thumbsHolder_ThumbOFF', audio7_html5_container).css({
										  'width':audio7_html5_container.width()+2*options.playerPadding-current_obj.audio7_html5_sliderVertical.width()-2*options.playlistPadding-3+'px'
									  });
								  } else {
									  $('.thumbsHolder_ThumbOFF', audio7_html5_container).css({
										  'width':audio7_html5_container.width()+2*options.playerPadding-2*options.playlistPadding+'px'
									  });
								  }


								  audio7_html5_search_term.css({
									  'width':(options.playerWidth-30-2*options.playlistPadding-7)+'px'
								  });
						  }

						  arrangePlayerElements(current_obj,options,audio7_html5_container,audio7_html5_thumbsHolder,audio7_html5_thumbsHolderWrapper,audio7_html5_thumbsHolderVisibleWrapper,audio7_html5_selectedCategDiv,audio7_html5_innerSelectedCategDiv,audio7_html5_searchDiv,audio7_html5_playlistPadding,audio7_html5_play_btn,audio7_html5_Audio_seek,audio7_html5_Audio_buffer,audio7_html5_Audio_timer_a,audio7_html5_Audio_timer_b,audio7_html5_Title,audio7_html5_TitleInside,audio7_html5_Author,audio7_html5_Audio,audio7_html5_ximage,audio7_html5_vinyl_record_on,audio7_html5_vinyl_record_off,audio7_html5_the_wrapper,audio7_html5_popup_btn,audio7_html5_the_bars,audio7_html5_volumeMute_btn,audio7_html5_volumeSlider,audio7_html5_shuffle_btn,audio7_html5_download_btn,audio7_html5_showHidePlaylist_btn,audio7_html5_lyrics_btn,audio7_html5_facebook_btn,audio7_html5_twitter_btn,audio7_html5_rewind_btn,audio7_html5_prev_btn,audio7_html5_next_btn,audio7_html5_buy_btn,audio7_html5_clearFavoritesBut);


						  if (options.playerWidth<$(window).width()) {
							  audio7_html5_container.parent().css({
								  'float':current_obj.origParentFloat,
								  'padding-top':current_obj.origParentPaddingTop,
								  'padding-right':current_obj.origParentPaddingRight,
								  'padding-bottom':current_obj.origParentPaddingBottom,
								  'padding-left':current_obj.origParentPaddingLeft
							  });
						  }


				  }
			};

			var TO = false;
			$(window).on( "resize", function() {
				var doResizeNow=true;

				if (ver_ie!=-1 && ver_ie==9 && current_obj.windowWidth==0)
					doResizeNow=false;


				if (current_obj.windowWidth==$(window).width()) {
					doResizeNow=false;
					if (options.windowCurOrientation!=window.orientation && navigator.userAgent.indexOf('Android') != -1) {
						options.windowCurOrientation=window.orientation;
						doResizeNow=true;
					}
				} else {
					/*if (current_obj.windowWidth===0 && (val.indexOf("ipad") != -1 || val.indexOf("iphone") != -1 || val.indexOf("ipod") != -1 || val.indexOf("webos") != -1))
						doResizeNow=false;*/
					current_obj.windowWidth=$(window).width();
				}

				if (options.responsive && doResizeNow) {
					 if(TO !== false)
						clearTimeout(TO);


					 TO = setTimeout(function(){ doResize() }, 300); //300 is time in miliseconds
				}
			});



			if (options.responsive) {
				doResize();
			}

			if (current_obj.cookie_popupWin && window.name!='audio7_PopupName') {
					audio7_html5_container.parent().remove();
					/*document.cookie = "cookie_popupWin=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
					current_obj.cookie_popupWin=null;*/
			}
			$(window).on("beforeunload", function() {
				if (window.name=='audio7_PopupName') {
						//document.cookie = "cookie_popupWin=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
						setCookie(options,'cookie_popupWin', current_obj.cookie_popupWin,1); //clear the cookie
						current_obj.cookie_popupWin=null;
						//return confirm("Do you really want to close?");
				}
			});


		});
	};


	//
	// plugin customization variables
	//
	$.fn.audio7_html5.defaults = {
		  playerWidth:500,//removed
			skin: 'whiteControllers',
			initialVolume:0.5,
			autoPlay:false,
			loop:true,
			shuffle:false,

			sticky:true,
			startMinified:false,

			playerPadding: 0, //removed
			playerBg: '#000000',
			bufferEmptyColor: '#929292',
			bufferFullColor: '#454545',
			seekbarColor: '#ffffff',
			volumeOffColor: '#454545',
			volumeOnColor: '#ffffff',
			timerColor: '#ffffff',
			songTitleColor: '#a6a6a6',
			songAuthorColor: '#ffffff',


			showVinylRecord:true,
			showRewindBut:true,
			showNextPrevBut:true,
			showShuffleBut:true,
			showDownloadBut:true,
			showBuyBut:true,
			showLyricsBut:true,
			buyButTitle:'Buy Now',
			lyricsButTitle:'Lyrics',
			buyButTarget:'_blank',
			lyricsButTarget:'_blank',
			showFacebookBut:true,
			facebookAppID:'',
			facebookShareTitle:'Apollo HTML5 Audio Player',
			facebookShareDescription:'A top-notch sticky full width HTML5 Audio Player compatible with all major browsers and mobile devices.',
			showTwitterBut:true,
			showPopupBut:true,
			showAuthor:true,
			showTitle:true,
			showPlaylistBut:true,
			showPlaylist:true,
			showPlaylistOnInit:false,

			showClearFavoritesBut:false,
			favoritesCookieExpirationDays:365, //in days 24 * 3600 * 1000

			playlistTopPos:2,
			playlistBgColor:'#000000',
			playlistRecordBgOffColor:'#000000',
			playlistRecordBgOnColor:'#333333',
			playlistRecordBottomBorderOffColor:'#333333',
			playlistRecordBottomBorderOnColor:'#4d4d4d',
			playlistRecordTextOffColor:'#777777',
			playlistRecordTextOnColor:'#FFFFFF',

			categoryRecordBgOffColor:'#191919',
			categoryRecordBgOnColor:'#252525',
			categoryRecordBottomBorderOffColor:'#2f2f2f',
			categoryRecordBottomBorderOnColor:'#2f2f2f',
			categoryRecordTextOffColor:'#4c4c4c',
			categoryRecordTextOnColor:'#00b4f9',

			numberOfThumbsPerScreen:7,
			playlistPadding:18,

			showCategories:true,
			firstCateg:'',
			selectedCategBg: '#333333',
			selectedCategOffColor: '#FFFFFF',
			selectedCategOnColor: '#00b4f9',
			selectedCategMarginBottom:12,

			showSearchArea:true,
			searchAreaBg: '#333333',
			searchInputText:'search...',
			searchInputBg:'#ffffff',
			searchInputBorderColor:'#333333',
			searchInputTextColor:'#333333',
			searchAuthor:true,


			googleTrakingOn:false,
			googleTrakingCode:'',

			responsive:true, //removed
			origWidth:0,

			continuouslyPlayOnAllPages:true,

			pathToDownloadFile:'',


			showPlaylistNumber:true,
			popupWidth:1100,
			popupHeight:500,

			barsColor:'#ffffff',

			is_lbgSite:false, //hidden



			isSliderInitialized:false,
			isProgressInitialized:false,
			isPlaylistSliderInitialized:false

	};

})(jQuery);
