package models.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * Created by acmac on 2017/01/12.
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf in = (ByteBuf) msg;
		try {
/*			while (in.isReadable()) {
				System.out.print((char) in.readByte());
				System.out.flush();
			}*/

			System.out.println(in.toString(CharsetUtil.US_ASCII));

//			System.out.println(in.toString(CharsetUtil.UTF_8));
		} finally {
			ReferenceCountUtil.release(msg);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// Close the connection when an exception is raised.
		cause.printStackTrace();
		ctx.close();
	}

}
