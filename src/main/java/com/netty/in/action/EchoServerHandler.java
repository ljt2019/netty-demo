package com.netty.in.action;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 应用程序的业务逻辑都是在 ChannelInboundHandler 中来处理的，业务罗的生命周期在ChannelInboundHandler中
 * 
 * @Description
 * @author Linjt
 * @Date 2018年7月24日
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * 此方法必需重写
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("服务器接收到：" + msg);
		ctx.write(msg);
		ctx.flush();
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
