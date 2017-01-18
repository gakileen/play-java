package models.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/**
 * Created by acmac on 2017/01/13.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf m = (ByteBuf) msg;

		try {
			long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000;
			System.out.println(new Date(currentTimeMillis));
			ctx.close();
		} finally {
			m.release();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable caught) {
		caught.printStackTrace();
		ctx.close();
	}

}
