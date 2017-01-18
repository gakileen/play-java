package models.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created by acmac on 2017/01/13.
 */
public class TImeClient {

	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 8080;

		EventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			Bootstrap b = new Bootstrap();
			b.group(workerGroup);
			b.channel(NioSocketChannel.class);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new TimeClientHandler());
				}
			});

			// Start the client
			ChannelFuture f = b.connect(host, port).sync();

			// Wait util the connection is closed.
			f.channel().closeFuture().sync();

		} finally {
			workerGroup.shutdownGracefully();
		}
	}

}
