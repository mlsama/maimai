<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
	   <meta http-equiv="pragma" content="no-cache"/>
	   <meta http-equiv="cache-control" content="no-cache"/>
	   <meta http-equiv="expires" content="0"/> 
	   <meta name="format-detection" content="telephone=no"/>  
	   <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
	   <link rel="stylesheet" href="/css/base.css"/>
	   <link href="/css/purchase.2012.css?v=201410141639" rel="stylesheet" type="text/css"/>
	   <title>我的购物车 - 买买商城</title>
   	</head>
<body> 
	 <!--shortcut start-->
	 <jsp:include page="../commons/shortcut.jsp" />
	 <script type="text/javascript" src="/js/jquery.price_format.2.0.min.js"></script>
	 <script type="text/javascript">
	  		var tt_cart = {
	  			itemNumChange : function(){
	  				$(".increment").click(function(){//＋
	  					var _thisInput = $(this).siblings("input");
	  					_thisInput.val(eval(_thisInput.val()) + 1);
	  					$.post("/cart/update/"+_thisInput.attr("itemId") + "/" + _thisInput.val(),function(data){
	  						tt_cart.refreshTotalPrice();
	  					});
	  				});
	  				$(".decrement").click(function(){//-
	  					var _thisInput = $(this).siblings("input");
	  					if(eval(_thisInput.val()) == 1){
	  						return ;
	  					}
	  					_thisInput.val(eval(_thisInput.val()) - 1);
	  					$.post("/cart/update/"+_thisInput.attr("itemId")+"/"+_thisInput.val(),function(data){
	  						tt_cart.refreshTotalPrice();
	  					});
	  				});
	  				$(".quantity-form .quantity-text").rnumber(1);//限制只能输入数字
	  				$(".quantity-form .quantity-text").change(function(){
	  					var _thisInput = $(this);
	  					$.post("/cart/update/"+_thisInput.attr("itemId")+"/"+_thisInput.val(),function(data){
	  						tt_cart.refreshTotalPrice();
	  					});
	  				});
	  			},
	  			refreshTotalPrice : function(){ //重新计算总价
	  				/** 定义总价 */
	  				var total = 0;
	  				/** 定义购买总数量 */
	  				var buyNum = 0;
	  				/** 查询所有的商品，并迭代，计算总价与统计购买总数量 */
	  				$(".quantity-form .quantity-text").each(function(i,e){
	  					var obj = $(e);
	  					total += (eval(obj.attr("itemPrice")) * 10000 * eval(obj.val())) / 10000;
	  					buyNum += eval(obj.val());
	  				});
	  				/** 设置显示的购买总数量 */
	  				$("#selectedCount").text(buyNum);
	  				/** 设置显示的购买总金额 */
	  				$(".totalSkuPrice").html(new Number(total/100).toFixed(2)).priceFormat({ //价格格式化插件
	  					 prefix: '￥',
	  					 thousandsSeparator: ',',
	  					 centsLimit: 2
	  				});
	  			}
	  		};

	  		$(function(){
	  			tt_cart.itemNumChange();
	  		});
	  	</script>
	<!--shortcut end-->
	<div class="w w1 header clearfix">
		<div id="logo"><a href="http://www.maimai.com/">
		<img clstag="clickcart|keycount|xincart|logo" src="/images/taotao-logo.gif" title="返回买买商城首页" alt="返回买买商城首页"/></a></div>
	    <div class="language"><a href="javascript:void(0);" onclick="toEnCart()"></a></div>
		<div class="progress clearfix">
			<ul class="progress-1">
				<li class="step-1"><b></b>1.我的购物车</li>
				<li class="step-2"><b></b>2.填写核对订单信息</li>
				<li class="step-3">3.成功提交订单</li>
			</ul>
		</div>
	</div>
	<div class="w cart">
		<div class="cart-hd group">
			<h2>我的购物车</h2>
			<span id="show2" class="fore"> <span>现在&nbsp;</span><a class="btn" href="javascript:goToLogin();"><span class="btn-text">登录</span></a><span>， 您购物车中的商品将被永久保存</span>
	 </span>
		</div>
		<div id="show">
		
	<div class="cart-frame">
	    <div class="tl"></div>
	    <div class="tr"></div>
	</div>
	<div class="cart-inner">
	    <div class="cart-thead clearfix">
	        <div class="column t-checkbox form">
	        <input data-cart="toggle-cb" name="toggle-checkboxes" id="toggle-checkboxes_up" type="checkbox" checked="" value=""/>
	        <label for="toggle-checkboxes_up">全选</label></div>
	        <div class="column t-goods">商品</div>
	        <div class="column t-price">买买价</div>
	        <div class="column t-promotion">优惠</div>
	        <div class="column t-inventory">库存</div>
	        <div class="column t-quantity">数量</div>
	        <div class="column t-action">操作</div>
	    </div>
	    <div id="product-list" class="cart-tbody">
	        <!-- ************************商品开始********************* -->
	        <c:set var="totalPrice" value="0"></c:set>
	        <c:set var="buyNum" value="0"></c:set>
	        <c:forEach items="${cart}" var="cartItem">
	        	<c:set var="totalPrice"  value="${ totalPrice + (cartItem.itemPrice * cartItem.num)}"/>
	        	<c:set var="buyNum"  value="${ buyNum + cartItem.num}"/>
	        	
		        <div id="product_11345721" data-bind="rowid:1" class="item item_selected ">
			        <div class="item_form clearfix">
			            <div class="cell p-checkbox">
			            	<input data-bind="cbid:1" class="checkbox" 
			            		type="checkbox" name="checkItem" checked="" value="11345721-1"/></div>
			            <div class="cell p-goods">
			                <div class="p-img">
			                	<a href="http://item.maimai.com/${cartItem.itemId }.html" target="_blank">
			                		<img clstag="clickcart|keycount|xincart|p-imglistcart" 
			                				src="${cartItem.itemImage}" alt="${cartItem.itemTitle}" width="52" height="52"/>
			                	</a>
			                </div>    
			                <div class="p-name">
			                	<a href="http://item.maimai.com/${cartItem.itemId }.html" clstag="clickcart|keycount|xincart|productnamelink" target="_blank">${cartItem.itemTitle}</a>
			                	<span class="promise411 promise411_11345721" id="promise411_11345721"></span>
			                </div>    
			            </div>
			            <div class="cell p-price"><span class="price">¥<fmt:formatNumber groupingUsed="false" value="${cartItem.itemPrice / 100}" maxFractionDigits="2" minFractionDigits="2"/> </span></div>
			            <div class="cell p-promotion">
			            </div>
			            <div class="cell p-inventory stock-11345721">有货</div>
			            <div class="cell p-quantity" for-stock="for-stock-11345721">
			                <div class="quantity-form" data-bind="">
			                    <a href="javascript:void(0);" class="decrement" clstag="clickcart|keycount|xincart|diminish1" id="decrement">-</a>
			                    <input type="text" class="quantity-text" 
			                    	itemPrice="${cartItem.itemPrice}" itemId="${cartItem.itemId}" 
			                    	value="${cartItem.num}" id="changeQuantity-11345721-1-1-0"/>
			                    <a href="javascript:void(0);" class="increment" clstag="clickcart|keycount|xincart|add1" id="increment">+</a>
			                </div>
			            </div>
			            <div class="cell p-remove"><a id="remove-11345721-1" data-more="removed-87.20-1" clstag="clickcart|keycount|xincart|btndel318558" class="cart-remove" href="/cart/delete/${cartItem.itemId}.html">删除</a>
			            </div>
			        </div>
		        </div> 
	        </c:forEach>
	        
	    </div><!-- product-list结束 -->
	          <div class="cart-toolbar clearfix">
	            <div class="total fr">
	                <p><span class="totalSkuPrice">¥<fmt:formatNumber value="${totalPrice / 100}" maxFractionDigits="2" minFractionDigits="2" groupingUsed="true"/></span>总计：</p>
	                <p><span id="totalRePrice">- ¥0.00</span>优惠：</p>
	            </div>
	            <div class="amout fr"><span id="selectedCount">${buyNum}</span> 件商品</div>
	        </div>
	        <div class="ui-ceilinglamp-1" style="width: 988px; height: 49px;"><div class="cart-dibu ui-ceilinglamp-current" style="width: 988px; height: 49px;">
	          <div class="control fdibu fdibucurrent">
	              <span class="column t-checkbox form">
	                  <input data-cart="toggle-cb" name="toggle-checkboxes" 
	                  	id="toggle-checkboxes_down" type="checkbox" checked="" value="" class="jdcheckbox"/>
	                  <label for="toggle-checkboxes_down">全选</label>
	              </span>
	              <span class="delete">
	                  <b></b>
	                  <a href="javascript:void(0);" clstag="clickcart|keycount|xincart|clearcartlink" id="remove-batch">
	                          删除选中的商品
	                  </a>
	              </span>
	              <span class="shopping">
	                  <b></b>
	                  <a href="http://www.maimai.com/" target="_blank" clstag="clickcart|keycount|xincart|coudanlink" id="continue">继续购物</a>
	              </span>
	          </div>
	          <div class="cart-total-2014">
	              <div class="cart-button">
	                  <span class="check-comm-btns" id="checkout-jd">
	                      <a class="checkout" href="/order/balance.html" clstag="clickcart|keycount|xincart|gotoOrderInfo" id="toSettlement">去结算<b></b></a>
	                  </span>
	                  <span class="combine-btns" style="display:none">
	                        <span class="fore1" style="display: none;">
	                          <a href="" class="combine-btn">不支持合并付款</a>
	                      </span>
	                      <span class="fore2 hide" style="display: inline;">
	                          <a href="javascript:goToOverseaOrder();" class="checkout-jdInt">去买买国际结算<b></b></a>
	                          <a href="javascript:goToOrder();" class="checkout-jd">去买买结算<b></b></a>
	                      </span>
	                  </span>
	              </div>
	              <div class="total fr">
	                  总计（不含运费）：
	                  <span class="totalSkuPrice">¥<fmt:formatNumber value="${totalPrice / 100}" maxFractionDigits="2" minFractionDigits="2" groupingUsed="true"/></span>
	              </div>
	          </div>
	      </div></div>
	</div><!-- cart-inner结束 -->
	</div>
	</div>
	
	<!-- footer start -->
	<jsp:include page="../commons/footer.jsp" />
	<!-- footer end -->
	
</body>
</html>