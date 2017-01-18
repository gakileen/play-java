package models.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by acmac on 2017/01/12.
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ctx.write(msg);
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}

}
