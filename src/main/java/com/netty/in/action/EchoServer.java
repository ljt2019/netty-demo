package com.netty.in.action;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 服务器
 * 
 * @Description
 * @author Linjt
 * @Date 2018年7月24日
 */
public class EchoServer {

	// 端口号
	private final int port;

	public EchoServer(int port) {
		this.port = port;
	}

	public void start() throws Exception {

		// 创建 NioEventLoopGroup 对象来处理事件，如接受新连接、接收数据、写数据等等
		EventLoopGroup group = new NioEventLoopGroup();
		try {

			// 创建 ServerBootstrap 实例来引导绑定和启动服务器
			ServerBootstrap bootstrap = new ServerBootstrap();
			// 指定 InetSocketAddress，服务器监听此端口
			bootstrap.group(group).channel(NioServerSocketChannel.class).localAddress(port)

					// 设置 childHandler 执行所有的连接请求
					.childHandler(new ChannelInitializer<Channel>() {
						@Override
						protected void initChannel(Channel ch) throws Exception {
							// 设置 childHandler 执行所有的连接请求
							ch.pipeline().addLast("decoder", new StringDecoder());
							ch.pipeline().addLast("encoder", new StringEncoder());
							ch.pipeline().addLast(new EchoServerHandler());
						}
					});

			// 都设置完毕了，最后调用 ServerBootstrap.bind() 方法来绑定服务器
			ChannelFuture f = bootstrap.bind().sync();
			System.out.println((EchoServer.class.getName() + "=== 开始监听 === on “" + f.channel().localAddress()));

			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}

	public static void main(String[] args) throws Exception {
		new EchoServer(8080).start();
	}

}
