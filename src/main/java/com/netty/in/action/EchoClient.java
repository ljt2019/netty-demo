package com.netty.in.action;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * 客户机
 * 
 * @Description
 * @author Linjt
 * @Date 2018年7月24日
 */
public class EchoClient {

	// 需要连接的主机
	private final String host;
	// 端口号
	private final int port;

	public EchoClient(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public void start() throws Exception {
		// 创建 EventLoopGroup 对象并设置到 Bootstrap 中，EventLoopGroup
		// 可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			// 创建 Bootstrap 对象用来引导启动客户端
			Bootstrap bootstrap = new Bootstrap();
			// 创建 InetSocketAddress 并设置到 Bootstrap 中，InetSocketAddress 是指定连接的服务器地址
			bootstrap.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(host, port))
					.handler(new ChannelInitializer<Channel>() {

						@Override
						protected void initChannel(Channel ch) throws Exception {
							// 添加一个 ChannelHandler，客户端成功连接服务器后就会被执行
							ChannelPipeline p = ch.pipeline();
							// p.addLast("decoder", new StringDecoder());// 解码
							// p.addLast("encoder", new StringEncoder());// 编码
							p.addLast(new EchoClientHandler());// 可以加多个handler
						}
					});

			// 调用 Bootstrap.connect()来连接服务器
			ChannelFuture f = bootstrap.connect().sync();

			// 发送到服务器的信息
			f.channel().writeAndFlush("Hello Netty Server ,I am a common client");

			// 最后关闭 EventLoopGroup 来释放资源
			f.channel().closeFuture().sync();

		} finally {
			group.shutdownGracefully().sync();
		}
	}

	public static void main(String[] args) throws Exception {
		new EchoClient("127.0.0.1", 8080).start();
	}

}
