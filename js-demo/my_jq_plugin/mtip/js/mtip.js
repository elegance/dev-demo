/*
 * Poshy Tip jQuery plugin v1.2+
 * 
 * 
 */

(function($) {
	
	$.Mtip = function(elm, options) {
		this.$elm = $(elm);
		this.options = options;
		this.content = options.content || this.$elm.attr(options.contentAttr);
		if (!this.content) {
			return;
		}
		this.$tip = $('<div style="position: absolute; display: none; border: 0px solid rgb(51, 51, 51);word-break:break-all;'
				+ 'white-space: nowrap; transition: left 0.4s, top 0.4s; -webkit-transition: left 0.4s, top 0.4s;filter: alpha(opacity=90);opacity:0.9;'
				+ 'background-color: rgb(76, 76, 76); border-top-left-radius: 4px; border-top-right-radius: 4px;'
				+ 'border-bottom-right-radius: 4px; border-bottom-left-radius: 4px; color: rgb(255, 255, 255); text-decoration: none;'
				+ 'padding: 5px; font-family: Arial, Verdana, sans-serif; font-size: 12px; line-height: 18px; font-style: normal; font-weight: normal;">'
				+ this.content
				+ '</div>').appendTo(document.body);
		this.tipOuterW = this.$tip.outerWidth();
		this.tipOuterH = this.$tip.outerHeight();
		this.init();
	};

	$.Mtip.prototype = {
		init: function() {
				this.$elm.bind({
					'mouseenter': $.proxy(this.mouseenter, this)
					,'mouseleave': $.proxy(this.mouseleave, this)
					//,'mousemove': $.proxy(this.mousemove, this)
				});
				this.$tip.bind({
					'mouseenter': $.proxy(this.mouseenter, this)
					,'mouseleave': $.proxy(this.mouseleave, this)
				});
		},

		mouseenter: function(e) {
			if (this.disabled)
				return true;
			if (this.stimeout) {
				clearTimeout(this.stimeout);
				this.stimeout = 0;
				return;
			}
			this.show();
		},
		mouseleave: function(e) {
			if (this.disabled)
				return true;
			this.stimeout = setTimeout($.proxy(this.hide, this) ,100);
		},
		show: function() {
			this.calcPos();
			this.$tip.css({ left: this.pos.l, top: this.pos.t, display: 'block'});
		},
		hide: function() {
			if (this.stimeout) {
				clearTimeout(this.stimeout);
				this.stimeout = 0;
			}
			this.$tip.css('display','none');
		},
		calcPos: function() {
			var pos = {l:0, t:0},
				elmOffset = this.$elm.offset(),
				elm = {
					l: elmOffset.left,
					t: elmOffset.top,
					w: this.$elm.outerWidth(),
					h: this.$elm.outerHeight()
				};
			pos.l = elm.l;
			pos.t = elm.t + elm.h;
			this.pos = pos;
		}
		
	};

	$.fn.mtip = function(options) {
		var opts = $.extend({}, $.fn.mtip.defaults, options);
		return this.each(function() {
			new $.Mtip(this, opts);
		});
	}

	$.fn.mtip.defaults = {
		content:	'',	//直接传内容显示，如果有值将忽略contentAttr
		contentAttr:	'title'//显示内容的属性
	};


})(jQuery);
