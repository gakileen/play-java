package models.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created by acmac on 2017/01/13.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx){

		final ByteBuf time = ctx.alloc().buffer(4);
		time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

		final ChannelFuture f = ctx.writeAndFlush(time);
		f.addListener(future -> {
			assert f == future;
			ctx.close();
		});

//		f.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable caught) {
		caught.printStackTrace();
		ctx.close();
	}

}
